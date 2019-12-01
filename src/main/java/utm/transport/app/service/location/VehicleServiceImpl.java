package utm.transport.app.service.location;

import com.rabbitmq.client.DeliverCallback;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import utm.transport.app.api.dto.location.PointDto;
import utm.transport.app.api.dto.location.RoutePathDto;
import utm.transport.app.api.dto.location.VehicleDto;
import utm.transport.app.api.dto.location.VehicleTrack;
import utm.transport.app.entity.location.PathStops;
import utm.transport.app.entity.location.Route;
import utm.transport.app.entity.location.RoutePath;
import utm.transport.app.exceptions.MessageRecieveException;
import utm.transport.app.listener.MessageListenerModule;
import utm.transport.app.processor.VehicleProcessor;
import utm.transport.app.repository.location.PathStopsRepository;
import utm.transport.app.repository.location.RoutePathRepository;
import utm.transport.app.repository.location.RouteRepository;
import utm.transport.app.repository.location.StopsRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VehicleServiceImpl implements VehicleService {
    private static final String UTF_8 = "UTF-8";

    private final PathStopsRepository pathStopsRepository;
    private final RoutePathRepository routePathRepository;
    private final RouteRepository routeRepository;
    private static MessageListenerModule messageListenerModule;
    private final ConcurrentHashMap<String, Queue<VehicleDto>> mq = new ConcurrentHashMap<>();
    private final HashMap<String, List<String>> vehiclesOnPath = new HashMap<>();

    public VehicleServiceImpl (PathStopsRepository pathStopsRepository, RoutePathRepository routePathRepository, RouteRepository routeRepository, StopsRepository stopsRepository) {
        this.pathStopsRepository = pathStopsRepository;
        this.routePathRepository = routePathRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<RoutePath> getRoutePath(String id) {
        return routePathRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<List<PathStops>> getRoutePathStops(String id) {
        return pathStopsRepository.findAllPathStops(id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void getStarted(String uid) throws MessageRecieveException {
        messageListenerModule = MessageListenerModule.init();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            VehicleDto vehicle = VehicleProcessor.transformToDto(new String(delivery.getBody(), UTF_8));
            try {
                if (mq.containsKey(vehicle.getTransportId())) {
                    if (mq.get(vehicle.getTransportId()) != null) {
                        mq.get(vehicle.getTransportId()).add(vehicle);
                        mq.get(vehicle.getTransportId()).remove();
                    }
                } else {
                    Queue<VehicleDto> vehicleDtos = new BlockingArrayQueue<>();
                    vehicleDtos.add(vehicle);
                    mq.put(vehicle.getTransportId(), vehicleDtos);
                }

                if (vehiclesOnPath.containsKey(vehicle.getPathId())) {
                    if (vehiclesOnPath.get(vehicle.getPathId()) != null)
                        vehiclesOnPath.get(vehicle.getPathId()).add(vehicle.getTransportId());
                } else {
                    List<String> transportIDs = new ArrayList<>();
                    transportIDs.add(vehicle.getTransportId());
                    vehiclesOnPath.put(vehicle.getPathId(), transportIDs);
                }

                System.out.println(String.format("Сообщение получено: %s", vehicle.getTransportId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        messageListenerModule.receiveMessages(uid, deliverCallback);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void abort(String uid) {
        messageListenerModule.abort(uid);
        mq.clear();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Route getRouteById(String id) {
        return routeRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<VehicleTrack> get(Double lat, Double lon) {
        List<VehicleTrack> listOfAverageData = new ArrayList<>();
        mq.forEach((k, v) -> {
            if (v.peek() != null) {
                String routeId = v.element().getPathId();
                RoutePathDto routePathDto = RoutePathDto.fromEntity(getRoutePath(routeId).get());
                Route route = getRouteById(routePathDto.getRouteId());
                VehicleTrack track = new VehicleTrack();
                track.setAverageSpeed(0.00);
                track.setTransportId(k);
                track.setRouteName(route.getName());
                track.setRouteNumber(route.getNumber());
                track.setFullTrack(routePathDto.getGeometry());
                List<PointDto> trackDots = new ArrayList<>();
                v.forEach((i->{
                    track.setAverageSpeed(track.getAverageSpeed() + i.getSpeed());
                    routePathDto.getGeometry().forEach((p)->{
                        if (Math.abs(p.getX() - i.getLon()) <= 0.015 && Math.abs(p.getY() - i.getLat()) <= 0.015) {
                            trackDots.add(p);
                        }
                    });
                }));
                track.setAverageSpeed(track.getAverageSpeed() / v.size());
                track.setTrack(trackDots);
                listOfAverageData.add(track);
            }
        });
        return listOfAverageData;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    private List<VehicleDto> getVehicleDtosByIDs(List<String> ids) {
        List<VehicleDto> vehicles = new ArrayList<>();
        for (String id : ids) {
            if (mq.containsKey(id) && mq.get(id) != null)
                vehicles.add(mq.get(id).peek());
        }
        return vehicles;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<VehicleDto> getVehiclesOnPath(String id) {
        return vehiclesOnPath.containsKey(id) ? getVehicleDtosByIDs(vehiclesOnPath.get(id)) : new ArrayList<>();
    }
}
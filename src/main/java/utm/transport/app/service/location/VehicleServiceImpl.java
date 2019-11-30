package utm.transport.app.service.location;

import com.rabbitmq.client.DeliverCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import utm.transport.app.api.dto.location.PointDto;
import utm.transport.app.api.dto.location.RoutePathDto;
import utm.transport.app.api.dto.location.VehicleDto;
import utm.transport.app.api.dto.location.VehicleTrack;
import utm.transport.app.entity.location.PathStops;
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
    private final StopsRepository stopsRepository;
    private static MessageListenerModule messageListenerModule;
    private final ConcurrentHashMap<String, List<VehicleDto>> mq = new ConcurrentHashMap<>();

    public VehicleServiceImpl (PathStopsRepository pathStopsRepository, RoutePathRepository routePathRepository, RouteRepository routeRepository, StopsRepository stopsRepository) {
        this.pathStopsRepository = pathStopsRepository;
        this.routePathRepository = routePathRepository;
        this.routeRepository = routeRepository;
        this.stopsRepository = stopsRepository;
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
        if (messageListenerModule == null)
            messageListenerModule = MessageListenerModule.init();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            VehicleDto vehicle = VehicleProcessor.transformToDto(new String(delivery.getBody(), UTF_8));
            if (mq.containsKey(vehicle.getTransportId())) {
                if (mq.get(vehicle.getTransportId()) != null) {
                    mq.get(vehicle.getTransportId()).add(vehicle);
                }
            } else mq.put(vehicle.getTransportId(), Arrays.asList(vehicle));

            System.out.println(String.format("Сообщение получено: %s", vehicle.getTransportId()));
        };

        messageListenerModule.receiveMessages(uid, deliverCallback);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void abort(String uid) {
        messageListenerModule.abort(uid);
        messageListenerModule = null;
        mq.clear();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<VehicleTrack> get(Double lat, Double lon) {
        List<VehicleTrack> listOfAverageData = new ArrayList<>();
        mq.forEach((k, v) -> {
            RoutePathDto routePathDto = RoutePathDto.fromEntity(getRoutePath(v.get(0).getPathId()).get());
            VehicleTrack track = new VehicleTrack();
            track.setAverageSpeed(0.00);
            track.setTransportId(k);
            List<PointDto> trackDots = new ArrayList<>();
            v.forEach((i->{
                if (VehicleProcessor.checkForLocation(lat, lon, i)) {
                    track.setAverageSpeed(track.getAverageSpeed() + i.getSpeed());
                    routePathDto.getGeometry().forEach((p)->{
                        if (Math.abs(p.getX() - i.getLon()) <= 0.002 && Math.abs(p.getY() - i.getLat()) <= 0.002) {
                            trackDots.add(p);
                        }
                    });
                }
            }));
            track.setAverageSpeed(track.getAverageSpeed() / v.size());
            track.setTrack(trackDots);
            listOfAverageData.add(track);
        });
        return listOfAverageData;
    }
}

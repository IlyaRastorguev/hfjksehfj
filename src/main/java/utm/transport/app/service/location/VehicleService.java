package utm.transport.app.service.location;

import utm.transport.app.api.dto.location.VehicleDto;
import utm.transport.app.api.dto.location.VehicleTrack;
import utm.transport.app.entity.location.PathStops;
import utm.transport.app.entity.location.Route;
import utm.transport.app.entity.location.RoutePath;
import utm.transport.app.exceptions.MessageRecieveException;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    Optional<RoutePath> getRoutePath(String id);
    Optional<List<PathStops>> getRoutePathStops(String id);
    void getStarted(String uid) throws MessageRecieveException;
    void abort(String uid);
    List<VehicleTrack> get(Double lat, Double lon);
    Route getRouteById(String uid);
    List<VehicleDto> getVehiclesOnPath(String id);
}
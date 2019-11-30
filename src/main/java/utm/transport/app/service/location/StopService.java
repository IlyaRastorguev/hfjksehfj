package utm.transport.app.service.location;

import utm.transport.app.entity.location.Stop;

import java.util.List;
import java.util.Optional;

public interface StopService {

    Optional<List<Stop>> getClosestStops(Double lat, Double lon);
}

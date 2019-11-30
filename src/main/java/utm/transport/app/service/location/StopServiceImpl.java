package utm.transport.app.service.location;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import utm.transport.app.entity.location.Stop;
import utm.transport.app.repository.location.PathStopsRepository;
import utm.transport.app.repository.location.StopsRepository;

import java.util.List;
import java.util.Optional;

public class StopServiceImpl implements StopService {

    private final PathStopsRepository pathStopsRepository;
    private final StopsRepository stopsRepository;

    public StopServiceImpl (PathStopsRepository pathStopsRepository, StopsRepository stopsRepository) {

        this.pathStopsRepository = pathStopsRepository;
        this.stopsRepository = stopsRepository;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<List<Stop>> getClosestStops(Double lat, Double lon) {
        return stopsRepository.findNearbyStops(lat, lon);
    }
}
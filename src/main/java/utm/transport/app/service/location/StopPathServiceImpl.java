package utm.transport.app.service.location;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import utm.transport.app.entity.location.PathStops;
import utm.transport.app.repository.location.PathStopsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StopPathServiceImpl implements StopPathService {

    private final PathStopsRepository pathStopsRepository;

    public StopPathServiceImpl(PathStopsRepository pathStopsRepository) {
        this.pathStopsRepository = pathStopsRepository;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<PathStops> getRoutesForStop(String id) {
        Optional<List<PathStops>> pathStops = pathStopsRepository.findAllPathsForStop(id);
        return pathStops.isPresent() ? pathStops.get() : new ArrayList<>();
    }
}
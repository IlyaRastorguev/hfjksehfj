package utm.transport.app.service.location;

import utm.transport.app.entity.location.PathStops;

import java.util.List;

public interface StopPathService {

    List<PathStops> getRoutesForStop(String id);
}
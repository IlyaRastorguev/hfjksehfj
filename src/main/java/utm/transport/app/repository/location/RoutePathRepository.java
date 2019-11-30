package utm.transport.app.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utm.transport.app.entity.location.RoutePath;

@Repository
public interface RoutePathRepository extends JpaRepository<RoutePath, String> {
}

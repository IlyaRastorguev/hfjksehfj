package utm.transport.app.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utm.transport.app.entity.location.Stops;

@Repository
public interface StopsRepository extends JpaRepository<Stops, String> {
}

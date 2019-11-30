package utm.transport.app.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utm.transport.app.entity.location.PathStops;

import java.util.List;
import java.util.Optional;

@Repository
public interface PathStopsRepository extends JpaRepository<PathStops, String> {

    @Query("SELECT p FROM PathStops p where p.pathId = :id")
    Optional<List<PathStops>> findAllPathStops(@Param("id") String id);
}

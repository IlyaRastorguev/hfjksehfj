package utm.transport.app.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utm.transport.app.entity.location.PathStops;
import utm.transport.app.entity.location.Stop;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopsRepository extends JpaRepository<Stop, String> {

//    @Query(value = "SELECT s FROM Stop s WHERE s.lat - :lat <= 0.000005 AND :lat - s.lat <= 0.000005 AND s.lon - :lon <= 0.000005 AND :lon - s.lon <= 0.000005")
    @Query(value = "SELECT s FROM Stop s WHERE ABS(s.lat - :lat) <= 0.015 AND ABS(s.lon - :lon) <= 0.015")
    Optional<List<Stop>> findNearbyStops(@Param("lat") Double lat, @Param("lon") Double lon);
}
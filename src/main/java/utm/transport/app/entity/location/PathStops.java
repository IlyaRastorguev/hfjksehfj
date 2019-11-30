package utm.transport.app.entity.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import utm.transport.app.core.entity.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "path_stop")
public class PathStops extends BaseEntity {
    @Column(name = "route_path_id")
    private String pathId;
    @Column(name = "stop_id")
    private String stopId;
    @Column(name = "path_geometry")
    private Geometry geometry;
    @Column(name = "path_geometry_json")
    private LineString stop;
}

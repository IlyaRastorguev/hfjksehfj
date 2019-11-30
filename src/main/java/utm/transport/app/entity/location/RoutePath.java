package utm.transport.app.entity.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import org.postgis.ComposedGeom;
import utm.transport.app.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "route_path")
public class RoutePath extends BaseEntity {

    @Column(name = "route_id")
    private String routePath;

    @Column(name = "path_geometry")
    private Geometry geometry;
}

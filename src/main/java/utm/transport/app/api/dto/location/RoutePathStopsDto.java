package utm.transport.app.api.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import utm.transport.app.entity.location.PathStops;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoutePathStopsDto {
    private String routePathId;
    private List<PointDto> stops;

    public static RoutePathStopsDto fromEntity (List<PathStops> stops) {
        RoutePathStopsDto dto = new RoutePathStopsDto();
        List<PointDto> pointDtos = new ArrayList<>(stops.size());

        stops.forEach((i)->{
            if (dto.getRoutePathId() == null)
                dto.setRoutePathId(i.getPathId());
            if (i.getGeometry() != null) {
                Coordinate coordinate = i.getGeometry().getCoordinate();
                pointDtos.add(PointDto.toDto(coordinate));
            }
        });

        dto.setStops(pointDtos);

        return dto;
    }
}

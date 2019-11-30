package utm.transport.app.api.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import utm.transport.app.entity.location.RoutePath;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class RoutePathDto {

    private String id;
    private String routeId;
    private List<PointDto> geometry;

    public static RoutePathDto fromEntity(RoutePath routePath) {
        RoutePathDto dto = new RoutePathDto();

        dto.setId(routePath.getId());
        dto.setRouteId(routePath.getRoutePath());
        List<PointDto> coordinates = new ArrayList<>();
        List<Coordinate> coordinatesList = Arrays.asList(routePath.getGeometry().getCoordinates());
        coordinatesList.forEach((i)->coordinates.add(PointDto.toDto(i)));
        dto.setGeometry(coordinates);

        return dto;
    }
}

package utm.transport.app.api.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;

@Getter
@Setter
@NoArgsConstructor
public class PointDto {
    private double x;
    private double y;

    public static PointDto toDto (Coordinate coordinate) {
        PointDto dto = new PointDto();

        dto.setX(coordinate.getX());
        dto.setY(coordinate.getY());

        return dto;
    }
}

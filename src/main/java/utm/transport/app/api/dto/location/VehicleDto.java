package utm.transport.app.api.dto.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDto {

    private String transportId;
    private String pathId;
    private String routeId;
    private Double lat;
    private Double lon;
    private double speed;

}

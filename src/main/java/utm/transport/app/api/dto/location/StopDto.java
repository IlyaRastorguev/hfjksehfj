package utm.transport.app.api.dto.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StopDto {

    private String stopId;
    private String stopName;
    private Double lat;
    private Double lon;
}
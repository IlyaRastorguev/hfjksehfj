package utm.transport.app.api.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VehicleTrack {
    List<PointDto> track;
    Double averageSpeed;
    String transportId;
    String routeName;
    String routeNumber;
    List<PointDto> fullTrack;
}

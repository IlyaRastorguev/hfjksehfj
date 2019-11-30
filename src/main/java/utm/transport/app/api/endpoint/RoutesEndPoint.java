package utm.transport.app.api.endpoint;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utm.transport.app.api.dto.location.RoutePathDto;
import utm.transport.app.api.dto.location.StatusMessageDto;
import utm.transport.app.api.dto.location.VehicleTrack;
import utm.transport.app.entity.location.RoutePath;
import utm.transport.app.entity.location.Stop;
import utm.transport.app.exceptions.MessageRecieveException;
import utm.transport.app.service.location.StopService;
import utm.transport.app.service.location.VehicleService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/vehicle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api("Locations information")
public class RoutesEndPoint {

    private VehicleService service;
    private StopService stopService;

    public RoutesEndPoint(VehicleService service, StopService stopService) {
        this.service = service;
        this.stopService = stopService;
    }

    @GetMapping("/route/path/{id}")
    public ResponseEntity<RoutePathDto> getVehicleInfo (@PathVariable("id") String id) {
        Optional<RoutePath> optionalRoutePath = service.getRoutePath(id);
        RoutePathDto dto = new RoutePathDto();
        if (optionalRoutePath.isPresent())
            dto = RoutePathDto.fromEntity(optionalRoutePath.get());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/route/start/{uid}")
    public ResponseEntity<StatusMessageDto> getStarted (@PathVariable("uid") String uid) {
        StatusMessageDto statusMessageDto = new StatusMessageDto();
        statusMessageDto.setStatusCode(0L);
        try {
            service.getStarted(uid);

            return ResponseEntity.ok(statusMessageDto);

        } catch (MessageRecieveException e) {
            statusMessageDto.setMessage(e.getMessage());

            return ResponseEntity.ok(statusMessageDto);
        }
    }

    @GetMapping("/route/stop/{uid}")
    public ResponseEntity<StatusMessageDto> getEnded (@PathVariable("uid") String uid) {
        StatusMessageDto statusMessageDto = new StatusMessageDto();
        statusMessageDto.setStatusCode(0L);
        service.abort(uid);

        return ResponseEntity.ok(statusMessageDto);
    }

    @GetMapping("/route/get/{lat}/{lon}/")
    public List<VehicleTrack> getCurrent (@PathVariable("lat") Double lat, @PathVariable("lon") Double lon) {
        return service.get(lat, lon);
    }

    @GetMapping("/stops/get/{lat}/{lon}/")
    public List<Stop> getCurrentStops (@PathVariable("lat") Double lat, @PathVariable("lon") Double lon) {
        return stopService.getClosestStops(lat, lon).get();
    }
}
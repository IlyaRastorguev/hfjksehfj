package utm.transport.app.processor;

import com.google.gson.Gson;
import utm.transport.app.api.dto.location.VehicleDto;

import java.util.List;

public class VehicleProcessor {

    private static final double radius = 0.5;

    public static void checkForLocation (List<VehicleDto> list, Double lat, Double lon, String data) {
        VehicleDto vehicleDto = new Gson().fromJson(data, VehicleDto.class);

        if (vehicleDto.getLon() - lon <=  radius && vehicleDto.getLat() - lat <= radius) {
            list.add(vehicleDto);
        }
    }
}

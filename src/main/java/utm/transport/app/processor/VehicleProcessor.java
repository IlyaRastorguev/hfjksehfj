package utm.transport.app.processor;

import com.google.gson.Gson;
import utm.transport.app.api.dto.location.VehicleDto;

public class VehicleProcessor {

    private static final double radius = 0.8;

    public static boolean checkForLocation (Double lat, Double lon, VehicleDto item) {
        return item.getLon() - lon <=  radius && item.getLat() - lat <= radius;
    }

    public static VehicleDto transformToDto(String jsonString) {
        return new Gson().fromJson(jsonString, VehicleDto.class);
    }
}

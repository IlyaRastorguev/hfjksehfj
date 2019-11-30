package utm.transport.app.config.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import utm.transport.app.repository.location.PathStopsRepository;
import utm.transport.app.repository.location.RoutePathRepository;
import utm.transport.app.repository.location.RouteRepository;
import utm.transport.app.repository.location.StopsRepository;
import utm.transport.app.service.location.VehicleService;
import utm.transport.app.service.location.VehicleServiceImpl;

import java.util.concurrent.TimeUnit;


@Configuration
public class ServiceConfig
{

    private final PathStopsRepository pathStopsRepository;
    private final RoutePathRepository routePathRepository;
    private final RouteRepository routeRepository;
    private final StopsRepository stopsRepository;

    public ServiceConfig(PathStopsRepository pathStopsRepository, RoutePathRepository routePathRepository, RouteRepository routeRepository, StopsRepository stopsRepository)
    {
        this.pathStopsRepository = pathStopsRepository;
        this.routePathRepository = routePathRepository;
        this.routeRepository = routeRepository;
        this.stopsRepository = stopsRepository;

    }

    @Bean
    public VehicleService vehicleService()
    {
        return new VehicleServiceImpl(pathStopsRepository, routePathRepository, routeRepository, stopsRepository);
    }

    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("vehicles");
    }

}

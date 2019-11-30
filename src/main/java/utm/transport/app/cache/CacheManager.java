package utm.transport.app.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class CacheManager {

    @Cacheable(value = "vehicles", key = "#key")
    public static String put (String key, String s) {
        return s;
    }

    @CacheEvict(value = "vehicles", key = "#key")
    public static void deleteAndEvict() {
        System.out.println("Кэш ошищен");
    }
}

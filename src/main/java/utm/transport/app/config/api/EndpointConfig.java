package utm.transport.app.config.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import utm.transport.app.config.service.ServiceConfig;
import utm.transport.app.core.api.SerializerConfiguration;

import java.util.List;


@EnableWebMvc
@Configuration
@Import(SerializerConfiguration.class)
@ComponentScan(basePackages = { "utm.transport.app.api.endpoint" })
public class EndpointConfig implements WebMvcConfigurer {
    private final ServiceConfig serviceConfig;
    private final ObjectMapper objectMapper;

    public EndpointConfig(ServiceConfig serviceConfig, ObjectMapper objectMapper) {
        this.serviceConfig = serviceConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                jsonMessageConverter.setObjectMapper(objectMapper);
                return;
            }
        }
    }
}

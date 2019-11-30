package utm.transport.app.config.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {
    private static final String BASE_ENDPOINT_PACKAGE = "utm.transport.app.api.endpoint";

    @Bean
    public Docket api_v1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("transport_app-api-1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_ENDPOINT_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo_v1());
    }

    private ApiInfo apiInfo_v1() {
        return new ApiInfoBuilder()
                .title("Wifi analyzer Rest API")
                .description("Service for analyze WIFI")
                .version("1.0")
                .contact(new Contact("Simanov A.N", "", "rest19930705@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
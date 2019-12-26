package lv.dp.education.swaper.rest;

import com.google.common.base.Predicates;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
@SwaggerDefinition(
        consumes = {"application/json"},
        produces = {"application/json"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS}
)
public class SwaggerConfig {
    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "Swaper",
            "Project aim is to show a sample p2p lending service", "0.0.1-SNAPSHOT",
            "urn:tos",
            new Contact("Deniss Prohorenko", "https://github.com/StrekoZ", "denpromail@gmail.com"),
            "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
            new ArrayList<>());

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .select().apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .build();
    }
}
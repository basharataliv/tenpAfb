package com.afba.imageplus.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    public static final Contact DEFAULT_CONTACT = new Contact("AFBA", "https://www.afba.com/", "1-800-776-2322");

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Image Plus API Documentation",
            "Swagger API documentation for AFBA ImagePlus Application", "1.0", "urn:tos", DEFAULT_CONTACT, "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0", Arrays.asList());

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).directModelSubstitute(LocalTime.class, String.class).select()
                .apis(RequestHandlerSelectors.basePackage("com.afba.imageplus")).paths(PathSelectors.any()).build()
                .apiInfo(DEFAULT_API_INFO).globalRequestParameters(getGlobalParameters());
    }

    public List<RequestParameter> getGlobalParameters() {
        // Adding Authorization Header
        RequestParameterBuilder authorizationHeader = new RequestParameterBuilder();
        authorizationHeader.name("Authorization").description("Bearer {{token}}").required(false).in("header")
                .accepts(Collections.singleton(MediaType.APPLICATION_JSON)).build();

        List<RequestParameter> aParameters = new ArrayList<>();
        aParameters.add(authorizationHeader.build());
        return aParameters;
    }

}

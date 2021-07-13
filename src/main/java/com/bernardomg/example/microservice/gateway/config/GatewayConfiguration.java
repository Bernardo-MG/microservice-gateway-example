
package com.bernardomg.example.microservice.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.example.microservice.gateway.config.properties.UriConfiguration;

@Configuration
public class GatewayConfiguration {

    public GatewayConfiguration() {
        super();
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder,
            UriConfiguration uriConfiguration) {
        final String httpUri;

        httpUri = uriConfiguration.getHttpbin();

        return builder.routes()
                .route(p -> p.path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri(httpUri))
                .build();
    }

}


package com.bernardomg.example.microservice.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.example.microservice.gateway.config.properties.UriConfiguration;

@Configuration
@EnableConfigurationProperties(UriConfiguration.class)
public class PropertiesConfiguration {

    public PropertiesConfiguration() {
        super();
    }

}

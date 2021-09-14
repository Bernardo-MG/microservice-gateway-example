
package com.bernardomg.example.microservice.gateway.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bernardomg.example.microservice.gateway.config.properties.UriConfiguration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class GatewayConfiguration {

    public GatewayConfiguration() {
        super();
    }

    @Bean
    public RouteLocator myRoutes(final RouteLocatorBuilder builder,
            final UriConfiguration uriConfiguration) {
        final String httpUri;

        httpUri = uriConfiguration.getHttpbin();

        return builder.routes()
                .route(p -> p.path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri(httpUri))
                .build();
    }

    @Bean
    @LoadBalanced
    RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Value("classpath:/static/index.html")
    private Resource indexHtml;

    /**
     * workaround solution for forwarding to index.html
     * 
     * @see <a href=
     *      "https://github.com/spring-projects/spring-boot/issues/9785">#9785</a>
     */
    @Bean
    RouterFunction<?> routerFunction() {
        final RouterFunction<?> router = RouterFunctions
                .resources("/**", new ClassPathResource("static/"))
                .andRoute(RequestPredicates.GET("/"),
                        request -> ServerResponse.ok()
                                .contentType(MediaType.TEXT_HTML)
                                .bodyValue(indexHtml));
        return router;
    }

    /**
     * Default Resilience4j circuit breaker configuration
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory>
            defaultCustomizer() {
        return factory -> factory
                .configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofSeconds(4)).build())
                        .build());
    }

}

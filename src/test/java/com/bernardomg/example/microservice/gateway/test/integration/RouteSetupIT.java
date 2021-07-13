
package com.bernardomg.example.microservice.gateway.test.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "httpbin=http://localhost:${wiremock.server.port}" })
@AutoConfigureWireMock(port = 0)
@DisplayName("Verifies the routes are built correctly")
public class RouteSetupIT {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("The get route works")
    public void buildsRoutes() throws Exception {
        // Stubs
        stubFor(get(urlEqualTo("/get")).willReturn(
                aResponse().withBody("{\"headers\":{\"Hello\":\"World\"}}")
                        .withHeader("Content-Type", "application/json")));

        webClient.get().uri("/get").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.headers.Hello").isEqualTo("World");
    }

}

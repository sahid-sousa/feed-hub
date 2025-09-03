package br.com.feedhub.integration.interfaces.controllers.security;

import br.com.feedhub.integration.config.IntegrationCommonsTest;
import br.com.feedhub.integration.config.TestConfigs;
import br.com.feedhub.interfaces.dto.request.security.AccountCredentials;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerTest extends IntegrationCommonsTest {

    @Test
    @Order(0)
    @DisplayName("POST /auth - should authenticate a user and returns a token")
    public void authenticate() {
        AccountCredentials credentials = new AccountCredentials("user-test-integration", "123456");
        var accessToken = given()
                .basePath("/auth")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenResponse.class);

        assertNotNull(accessToken);
        assertNotNull(accessToken.token());
    }

}

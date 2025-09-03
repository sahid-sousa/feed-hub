package br.com.feedhub.integration.config;

import br.com.feedhub.interfaces.dto.request.security.AccountCredentials;
import br.com.feedhub.interfaces.dto.request.user.UserCreateRequest;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

public class IntegrationCommonsTest extends AbstractIntegrationTest {

    protected static RequestSpecification specification;
    protected static String token;

    @BeforeEach
    void setup() {
        if (token == null) {
            UserCreateRequest user = new UserCreateRequest(
                    "User",
                    "user-test-integration",
                    "123456",
                    "email@email.com.br"
            );
            var userResponse = given()
                    .basePath("/user/create")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(user)
                    .when()
                    .post()
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(UserResponse.class);

            Assertions.assertNotNull(userResponse);
            Assertions.assertEquals("User", userResponse.getName());
            Assertions.assertEquals("user-test-integration", userResponse.getUsername());

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

            token = accessToken.token();

            specification = new RequestSpecBuilder()
                    .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token)
                    .setBasePath("/user")
                    .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();

            Assertions.assertNotNull(accessToken);
            Assertions.assertNotNull(accessToken.token());
        }

    }
}

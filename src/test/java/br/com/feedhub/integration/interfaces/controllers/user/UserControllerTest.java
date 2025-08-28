package br.com.feedhub.integration.interfaces.controllers.user;

import br.com.feedhub.integration.config.TestConfigs;
import br.com.feedhub.interfaces.dto.request.security.AccountCredentials;
import br.com.feedhub.interfaces.dto.request.user.UserCreateRequest;
import br.com.feedhub.interfaces.dto.request.user.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerTest {

    private static RequestSpecification specification;

    @Test
    @Order(0)
    @DisplayName("POST /user/create - should create a user")
    public void create() {
        UserCreateRequest user = new UserCreateRequest(
                "User",
                "user-test",
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

        assertNotNull(userResponse);
        assertEquals("User", userResponse.getName());
        assertEquals("user-test", userResponse.getUsername());
    }

    @Test
    @Order(1)
    @DisplayName("POST /auth - should authenticate a user and returns a token")
    public void authenticate() {
        AccountCredentials credentials = new AccountCredentials("user-test", "123456");
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

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken.token())
                .setBasePath("/user")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(accessToken);
        assertNotNull(accessToken.token());
    }

    @Test
    @Order(2)
    @DisplayName("PUT /update - should update a user")
    public void update() {
        UserUpdateRequest user = new UserUpdateRequest(
                "User",
                "456789",
                "user@email.com.br"
        );

        var userResponse = given().spec(specification)
                .basePath("/user/update")
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().body()
                .as(UserResponse.class);


        assertNotNull(userResponse);
        assertEquals("User", userResponse.getName());
        assertEquals("user-test", userResponse.getUsername());
        assertEquals("user@email.com.br", userResponse.getEmail());

    }

    @Test
    @Order(3)
    @DisplayName("GET /find/{id} - should find a user")
    public void find() {
        var userResponse = given().spec(specification)
                .basePath("/user/find/1")
                .port(TestConfigs.SERVER_PORT)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(UserResponse.class);

        assertNotNull(userResponse);
        assertEquals("User", userResponse.getName());
        assertEquals("user-test", userResponse.getUsername());
    }

    @Test
    @Order(4)
    @DisplayName("GET /list - should list users")
    public void list() {
        var userList = given().spec(specification)
                .basePath("/user/list?name=User&username=user-test")
                .port(TestConfigs.SERVER_PORT)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PageListResponse.class);

        assertNotNull(userList);
        assertEquals(1, userList.content().size());
        assertEquals(1, userList.totalElements());
        assertEquals(1, userList.totalPages());
    }

}

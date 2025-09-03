package br.com.feedhub.integration.interfaces.controllers.user;

import br.com.feedhub.integration.config.IntegrationCommonsTest;
import br.com.feedhub.integration.config.TestConfigs;
import br.com.feedhub.interfaces.dto.request.user.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerTest extends IntegrationCommonsTest {

    protected static Long userId;

    @Test
    @Order(0)
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
                .port(TestConfigs.SERVER_PORT)
                .body(user)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().body()
                .as(UserResponse.class);

        userId = userResponse.getId();

        assertNotNull(userResponse);
        assertEquals("User", userResponse.getName());
        assertEquals("user-test-integration", userResponse.getUsername());
        assertEquals("user@email.com.br", userResponse.getEmail());

    }

    @Test
    @Order(1)
    @DisplayName("GET /find/{id} - should find a user")
    public void find() {
        var userResponse = given().spec(specification)
                .basePath("/user/find/" + userId)
                .port(TestConfigs.SERVER_PORT)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(UserResponse.class);

        assertNotNull(userResponse);
        assertEquals("User", userResponse.getName());
        assertEquals("user-test-integration", userResponse.getUsername());
    }

    @Test
    @Order(2)
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
        assertFalse(userList.content().isEmpty());
        assertTrue(userList.totalElements() > 0);
        assertTrue(userList.totalPages() > 0);
    }

}

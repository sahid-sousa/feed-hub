package br.com.feedhub.integration.interfaces.controllers.feedback;

import br.com.feedhub.integration.config.IntegrationCommonsTest;
import br.com.feedhub.integration.config.TestConfigs;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackCreateRequest;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackUpdateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FeedbackControllerTest extends IntegrationCommonsTest {

    protected static Long feedbackId;

    @Test
    @Order(0)
    @DisplayName("POST /feedback/create - should create a new feedback")
    public void createFeeback() {

        FeedbackCreateRequest feedbackCreateRequest = new FeedbackCreateRequest(
                "Title",
                "Description",
                "QUESTION",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        var feedbackResponse = given().spec(specification)
                .basePath("/feedback/create")
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .port(TestConfigs.SERVER_PORT)
                .body(feedbackCreateRequest)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body()
                .as(FeedbackResponse.class);

        feedbackId = feedbackResponse.getId();

        assertNotNull(feedbackResponse);
        assertEquals("Title", feedbackResponse.getTitle());
        assertEquals("Description", feedbackResponse.getDescription());
        assertEquals("QUESTION", feedbackResponse.getCategory());

    }

    @Test
    @Order(1)
    @DisplayName("PUT /feedback/update - should update a feedback")
    public void updateFeeback() {
        FeedbackUpdateRequest feedbackUpdateRequest = new FeedbackUpdateRequest(
                "Title Updated",
                "Description Updated",
                "QUESTION",
                LocalDateTime.now(),
                LocalDateTime.now(),
                feedbackId
        );

        var feedbackResponse = given().spec(specification)
                .basePath("/feedback/update")
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .port(TestConfigs.SERVER_PORT)
                .body(feedbackUpdateRequest)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().body()
                .as(FeedbackResponse.class);

        assertNotNull(feedbackResponse);
    }

    @Test
    @Order(2)
    @DisplayName("GET /feedback/list - should list feedbacks")
    public void listFeeback() {
        var feedbackList = given().spec(specification)
                .basePath("/feedback/list?title=Title%Updated&description=Description%Updated&category=QUESTION&status=NEW")
                .port(TestConfigs.SERVER_PORT)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PageListResponse.class);

        assertNotNull(feedbackList);
        assertFalse(feedbackList.content().isEmpty());
        assertTrue(feedbackList.totalElements() > 0);
        assertTrue( feedbackList.totalPages() > 0);

    }
}

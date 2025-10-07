package br.com.feedhub.integration.interfaces.controllers.comment;

import br.com.feedhub.integration.config.IntegrationCommonsTest;
import br.com.feedhub.integration.config.TestConfigs;
import br.com.feedhub.interfaces.dto.request.comment.CommentCreateRequest;
import br.com.feedhub.interfaces.dto.request.comment.CommentUpdateRequest;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackCreateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CommentControllerTest extends IntegrationCommonsTest {

    protected static Long feedbackId;
    protected static Long commentId;

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
    @DisplayName("POST /comment/create - Should create a new comment")
    public void createComment() {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(
                feedbackId,
                "Parou de executar"
        );

        var commentResponse = given().spec(specification)
                .basePath("/comment/create")
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .port(TestConfigs.SERVER_PORT)
                .body(commentCreateRequest)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body()
                .as(CommentResponse.class);

        commentId = commentResponse.getId();

        assertNotNull(commentResponse);
        assertEquals("Parou de executar", commentResponse.getContent());

    }

    @Test
    @Order(2)
    @DisplayName("PUT /comment/update - Should update a new comment")
    public void updateComment() {
        CommentUpdateRequest commentUpdateRequest = new CommentUpdateRequest(
                commentId,
                "Funcionou corretamente"
        );

        var commentResponse = given().spec(specification)
                .basePath("/comment/update")
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .port(TestConfigs.SERVER_PORT)
                .body(commentUpdateRequest)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().body()
                .as(CommentResponse.class);

        assertNotNull(commentResponse);
        assertEquals("Funcionou corretamente", commentResponse.getContent());
    }

    @Test
    @Order(3)
    @DisplayName("GET /comment/list - Should list a comments")
    public void listComment() {
        var commentList = given().spec(specification)
                .basePath("/comment/list/" + feedbackId)
                .port(TestConfigs.SERVER_PORT)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PageListResponse.class);

        assertNotNull(commentList);
        assertEquals(1, commentList.content().size());
        assertEquals(1, commentList.totalElements());
        assertEquals(1, commentList.totalPages());
    }

}

package br.com.feedhub.interfaces.controllers.comment;

import br.com.feedhub.application.usecases.comment.CreateComment;
import br.com.feedhub.application.usecases.comment.UpdateComment;
import br.com.feedhub.interfaces.dto.request.comment.CommentCreateRequest;
import br.com.feedhub.interfaces.dto.request.comment.CommentUpdateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CreateComment createComment;

    @Mock
    private UpdateComment updateComment;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    CommentController commentController;

    private CommentCreateRequest commentCreateRequest;
    private CommentUpdateRequest commentUpdateRequest;
    private CommentResponse commentResponse;

    @BeforeEach
    public void setup() {
        commentCreateRequest = new CommentCreateRequest(1L, "comment created");
        commentUpdateRequest = new CommentUpdateRequest(1L, "comment updated");
        commentResponse = new CommentResponse(
                1L,
                "comment created",
                1L,
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Test Receive CommentCreateRequest when Create Comment then Return CommentResponse")
    void testReceiveCommentCreateRequest_whenCreateComment_thenReturnCommentResponse() {
        //Given
        given(createComment.execute(any(CommentCreateRequest.class), any(HttpServletRequest.class)))
                .willReturn(commentResponse);

        //When
        ResponseEntity<?> responseEntity = commentController.create(commentCreateRequest, request);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(commentResponse, responseEntity.getBody());
        assertEquals("comment created", commentResponse.getContent());
    }

    @Test
    @DisplayName("Test Receive CommentUpdateRequest when Update Comment then Return CommentResponse")
    void testReceiveCommentUpdateRequest_whenUpdateComment_thenReturnCommentResponse() {
        //Given
        commentResponse.setContent("comment updated");
        given(updateComment.execute(any(CommentUpdateRequest.class), any(HttpServletRequest.class)))
                .willReturn(commentResponse);

        //When
        ResponseEntity<?> responseEntity = commentController.update(commentUpdateRequest, request);

                //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(commentResponse, responseEntity.getBody());
        assertEquals("comment updated", commentResponse.getContent());
    }

}
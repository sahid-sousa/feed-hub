package br.com.feedhub.unit.application.usecases.comment;

import br.com.feedhub.application.usecases.comment.CreateComment;
import br.com.feedhub.interfaces.dto.request.comment.CommentCreateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CreateCommentTest {

    @Mock
    CreateComment createComment;

    @Mock
    private HttpServletRequest request;

    private CommentCreateRequest commentCreateRequest;
    private CommentResponse commentResponse;

    @BeforeEach
    public void setup() {
        //Given
        commentCreateRequest = new CommentCreateRequest(1L, "Parou de executar");
        commentResponse = new CommentResponse(
                1L,
                "Parou de executar",
                1L,
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Test Receive CommentCreateRequest when Create Comment then Return CommentResponse")
    void testReceiveCommentCreateRequest_whenCreate_theReturnCommentResponse() {
        //Given
        given(createComment.execute(commentCreateRequest, request)).willReturn(commentResponse);

        //When
        CommentResponse response = createComment.execute(commentCreateRequest, request);

        //Then
        assertNotNull(response);
    }

    @Test
    @DisplayName("Test Receive CommentCreateRequest when reate then Return Exception")
    void testReceiveCommentCreateRequest_whenCreate_thenReturnException() {
        //Given
        given(createComment.execute(commentCreateRequest, request)).willThrow(new RequiredObjectIsNullException("Required Object is null"));

        //When
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> createComment.execute(commentCreateRequest, request)
        );

        //Then
        assertEquals("Required Object is null", exception.getMessage());
    }

}
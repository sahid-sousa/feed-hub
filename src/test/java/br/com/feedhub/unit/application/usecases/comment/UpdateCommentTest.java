package br.com.feedhub.unit.application.usecases.comment;

import br.com.feedhub.application.usecases.comment.UpdateComment;
import br.com.feedhub.interfaces.dto.request.comment.CommentUpdateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateCommentTest {

    @Mock
    UpdateComment updateComment;

    @Mock
    private HttpServletRequest request;

    private CommentUpdateRequest commentUpdateRequest;
    private CommentResponse commentResponse;

    @BeforeEach
    public void setup() {
        //Given
        commentUpdateRequest = new CommentUpdateRequest(1L, "Funcionou corretamente");
        commentResponse = new CommentResponse(
                1L,
                "Funcionou corretamente agora",
                1L,
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Test Receive CommentUpdateRequest when UpdateComment then Return CommentResponse")
    void testReceiveCommentCreateRequest_whenCreateComment_thenReturnCommentResponse() {
        //Given
        given(updateComment.execute(commentUpdateRequest, request))
                .willReturn(commentResponse);
        //When
        CommentResponse response = updateComment.execute(commentUpdateRequest, request);
        //Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Funcionou corretamente agora", response.getContent());
        assertEquals(1L, response.getAuthorId());
        assertEquals(1L, response.getFeedbackId());
    }

}
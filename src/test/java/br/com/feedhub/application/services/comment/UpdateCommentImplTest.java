package br.com.feedhub.application.services.comment;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UpdateCommentImplTest {

    @InjectMocks
    UpdateCommentImpl updateComment;

    @Mock
    CommentGateway commentGateway;

    @Mock
    ExtractUsername extractUsername;

    @Mock
    UserGateway userGateway;

    @Mock
    private HttpServletRequest request;

    private User user;
    private Comment comment;
    private CommentUpdateRequest commentUpdateRequest;
    private String bearerToken;

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setId(1L);
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setAuthor(user);
        feedback.setTitle("Title");
        feedback.setDescription("Description");
        feedback.setCategory(FeedbackCategory.QUESTION);
        feedback.setStatus(FeedbackStatus.NEW);

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Comentario do feedback");
        comment.setAuthor(user);
        comment.setFeedback(feedback);
        comment.setDateCreated(LocalDateTime.now());
        comment.setLastUpdated(LocalDateTime.now());

        commentUpdateRequest = new CommentUpdateRequest(1L, "Funcionou corretamente");
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
        bearerToken =  "Bearer " + token;
    }

    @Test
    @DisplayName("Test Receive CommentUpdateRequest when UpdateComment then Return CommentResponse")
    void testReceiveCommentCreateRequest_whenCreateComment_thenReturnCommentResponse() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(bearerToken)).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(commentGateway.findByIdAndAuthor(anyLong(), any())).willReturn(Optional.of(comment));
        given(commentGateway.save(any())).willReturn(comment);

        //When
        CommentResponse response =  updateComment.execute(commentUpdateRequest, request);

        //Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Funcionou corretamente", response.getContent());
        assertEquals(1L, response.getAuthorId());
        assertEquals(1L, response.getAuthorId());
    }

}
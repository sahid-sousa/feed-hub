package br.com.feedhub.application.services.comment;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.comment.CommentCreateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
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
class CreateCommentImplTest {

    @InjectMocks
    CreateCommentImpl createComment;

    @Mock
    ExtractUsername extractUsername;

    @Mock
    CommentGateway commentGateway;

    @Mock
    FeedbackGateway feedbackGateway;

    @Mock
    UserGateway userGateway;

    @Mock
    private HttpServletRequest request;

    private User user;
    private Comment comment;
    private Feedback feedback;
    private CommentCreateRequest commentCreateRequest;

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

        feedback = new Feedback();
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


        commentCreateRequest = new CommentCreateRequest(1L, "Comentario do feedback" );
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
        bearerToken =  "Bearer " + token;
    }

    @Test
    @DisplayName("Test receive CommentCreateRequest when Create Comment then Return CommentResponse")
    void testReceiveCommentCreateRequest_whenCreate_thenReturnCommentResponse() {
        // Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(bearerToken)).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(feedbackGateway.findById(anyLong())).willReturn(Optional.of(feedback));
        given(commentGateway.save(any())).willReturn(comment);

        //When
        CommentResponse response = createComment.execute(commentCreateRequest, request);

        //Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Comentario do feedback", response.getContent());
        assertEquals(1L, response.getAuthorId());
        assertEquals(1L, response.getAuthorId());
    }

    @Test
    @DisplayName("Test receive CommentCreateRequest when create then throw Exception when User is null")
    void testReceiveCommentCreateRequest_whenCreate_thenThrowExceptionWhenUserIsNull() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(bearerToken)).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.empty());

        //When
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> createComment.execute(commentCreateRequest, request)
        );

        //Then
        assertEquals("User not found with user-test", exception.getMessage());
    }

    @Test
    @DisplayName("Test receive CommentCreateRequest when create then throw Exception when Feedback is null")
    void testReceiveCommentCreateRequest_whenCreate_thenThrowExceptionWhenFeedbackIsNull() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(bearerToken)).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(feedbackGateway.findById(anyLong())).willReturn(Optional.empty());

        //When
        RequiredObjectIsNullException exception = assertThrows(
          RequiredObjectIsNullException.class,
                () -> createComment.execute(commentCreateRequest, request)
        );

        //Then
        assertEquals("Feedback not found with id 1", exception.getMessage());
    }

}

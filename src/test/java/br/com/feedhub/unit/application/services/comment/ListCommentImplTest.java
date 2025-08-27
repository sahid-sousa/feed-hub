package br.com.feedhub.unit.application.services.comment;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.services.comment.ListCommentImpl;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ListCommentImplTest {

    @InjectMocks
    private ListCommentImpl listComment;

    @Mock
    CommentGateway commentGateway;

    @Mock
    ExtractUsername extractUsername;

    @Mock
    FeedbackGateway feedbackGateway;

    @Mock
    UserGateway userGateway;

    @Mock
    HttpServletRequest request;

    private Comment comment;
    private Feedback feedback;
    private User user;
    private Pageable pageable;
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

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "dateCreated");
        pageable = PageRequest.of(0, 10, sort);

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
        bearerToken =  "Bearer " + token;


    }

    @Test
    @DisplayName("Test receive params when  ListComment then return PageListResponse")
    void testReceiveParams_whenListComment_thenReturnPageListResponse() {
        //given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(feedbackGateway.findById(anyLong())).willReturn(Optional.of(feedback));
        List<Comment> comments = List.of(comment);
        Page<Comment> expectedPage = new PageImpl<>(comments, pageable, comments.size());
        given(commentGateway.findAllByFilters(any(), any())).willReturn(expectedPage);

        //When
        PageListResponse<CommentResponse> response = listComment.execute(
                1L,
                0,
                10,
                "dateCreated",
                "desc",
                request
        );

        //Then
        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(1, response.totalElements());
        assertEquals(10, response.size());
    }

    @Test
    @DisplayName("Test receive params when ListUserFeedback throw Exception when User is null")
    void testReceiveParams_whenListComment_thenThrowExceptionWhenFeedbackIsNull() {
        //Given
        given(feedbackGateway.findById(anyLong())).willReturn(Optional.empty());

        //When
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> listComment.execute(1L, 0, 10, "dateCreated", "desc", request)

        );

        //Then
        assertEquals("Feedback object with id 1 is null", exception.getMessage());

    }

    @Test
    @DisplayName("Test receive params when ListComment then throw Exception when User is null")
    void testReceiveParams_whenListComment_thenThrowExceptionWhenUserIsNull() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.empty());
        given(feedbackGateway.findById(anyLong())).willReturn(Optional.of(feedback));

        //When
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> listComment.execute(1L, 0, 10, "dateCreated", "desc", request)
        );

        //Then
        assertEquals("User object with username user-test is null", exception.getMessage());
    }
}
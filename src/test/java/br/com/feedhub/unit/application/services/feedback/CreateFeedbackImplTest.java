package br.com.feedhub.unit.application.services.feedback;

import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.services.feedback.CreateFeedbackImpl;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackCreateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateFeedbackImplTest {

    @InjectMocks
    CreateFeedbackImpl createFeedback;

    @Mock
    ExtractUsername extractUsername;

    @Mock
    UserGateway userGateway;

    @Mock
    FeedbackGateway feedbackGateway;

    @Mock
    HttpServletRequest request;

    private User user;
    private Feedback feedback;
    private FeedbackCreateRequest feedbackCreateRequest;
    private String bearerToken;

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        feedback = new Feedback();
        feedback.setAuthor(user);
        feedback.setTitle("Title");
        feedback.setDescription("Description");
        feedback.setCategory(FeedbackCategory.QUESTION);
        feedback.setStatus(FeedbackStatus.NEW);

        feedbackCreateRequest = new FeedbackCreateRequest(
                "Title",
                "Description",
                "QUESTION",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
        bearerToken =  "Bearer " + token;
    }

    @Test
    @DisplayName("Test receive FeedbackCreateRequest when create then return FeedbackResponse")
    void testReceiveFeedbackCreateRequest_whenCreate_thenReturnFeedbackResponse() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(feedbackGateway.save(any(Feedback.class))).willReturn(feedback);

        //When
        FeedbackResponse response = createFeedback.execute(feedbackCreateRequest, request);

        //Then
        assertNotNull(response);
        assertEquals("Title", response.getTitle());
        assertEquals("Description", response.getDescription());
    }

    @Test
    @DisplayName("Test receive FeedbackCreateRequest when create then throw Exception whenUser is null")
    void testReceiveFeedbackCreateRequest_whenCreate_thenThrowExceptionWhenUserIsNull() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.empty());

        //When
        RequiredObjectIsNullException exception = assertThrows(
          RequiredObjectIsNullException.class,
                () -> createFeedback.execute(feedbackCreateRequest, request)
        );

        //Then
        assertEquals("Author with username user-test not found", exception.getMessage());
    }

}
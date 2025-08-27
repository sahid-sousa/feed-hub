package br.com.feedhub.unit.application.services.feedback;

import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.services.feedback.UpdateFeedbackImpl;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackUpdateRequest;
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
class UpdateFeedbackImplTest {

    @InjectMocks
    UpdateFeedbackImpl updateFeedback;

    @Mock
    ExtractUsername extractUsername;

    @Mock
    FeedbackGateway feedbackGateway;

    @Mock
    UserGateway userGateway;

    @Mock
    HttpServletRequest request;

    private User user;
    private Feedback feedback;
    private FeedbackUpdateRequest feedbackUpdateRequest;
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

        feedbackUpdateRequest = new FeedbackUpdateRequest(
                "Title Updated",
                "Description Updated",
                "QUESTION",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L
        );

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
        bearerToken =  "Bearer " + token;
    }

    @Test
    @DisplayName("Test Receive FeedbackUpdateRequest when update then Return FeedbackResponse")
    void testReceiveFeedbackUpdateRequest_whenUpdate_thenReturnFeedbackResponse() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(bearerToken)).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(feedbackGateway.findByIdAndAuthor(anyLong(), any(User.class))).willReturn(Optional.of(feedback));
        given(feedbackGateway.save(any(Feedback.class))).willReturn(feedback);

        //When
        FeedbackResponse response = updateFeedback.execute(feedbackUpdateRequest, request);

        //Then
        assertNotNull(response);
        assertEquals("Title Updated", response.getTitle());
        assertEquals("Description Updated", response.getDescription());
    }

    @Test
    @DisplayName("Test receive FeedbackUpdateRequest when Update then throw Exception when User is null")
    void testReceiveFeedbackUpdateRequest_whenUpdate_thenThrowExceptionWhenUserIsNull() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(bearerToken)).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.empty());

        //When
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> updateFeedback.execute(feedbackUpdateRequest, request)
        );

        //Then
        assertEquals("Username user-test not exists", exception.getMessage());
    }

    @Test
    @DisplayName("Test receive FeedbackUpdateRequest when Update then throw Exception when Feedback is null")
    void testReceiveFeedbackUpdateRequest_whenUpdate_thenThrowExceptionWhenFeedbackIsNull() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(bearerToken)).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(feedbackGateway.findByIdAndAuthor(anyLong(), any(User.class))).willReturn(Optional.empty());

        //When
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> updateFeedback.execute(feedbackUpdateRequest, request)
        );

        //Then
        assertEquals("Feedback with id 1 not exists", exception.getMessage());
    }

}
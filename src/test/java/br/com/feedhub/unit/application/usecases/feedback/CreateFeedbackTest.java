package br.com.feedhub.unit.application.usecases.feedback;

import br.com.feedhub.application.usecases.feedback.CreateFeedback;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackCreateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CreateFeedbackTest {

    @Mock
    CreateFeedback createFeedback;

    @Mock
    private HttpServletRequest request;

    private FeedbackCreateRequest feedbackCreateRequest;
    private FeedbackResponse feedbackResponse;

    @BeforeEach
    public void setup() {
        //Given
        User user = new User();
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        Feedback feedback = new Feedback();
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

        feedbackResponse = new FeedbackResponse(
                "Title",
                "Description",
                "QUESTION",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                1L,
                "NEW"
        );
    }

    @Test
    @DisplayName("Test receive FeedbackCreateRequest when create then return FeedbackResponse")
    void testReceiveFeedbackCreateRequest_whenCreate_thenReturnFeedbackResponse() {
        //Given
        given(createFeedback.execute(any(), any())).willReturn(feedbackResponse);

        //When
        FeedbackResponse feedbackResponse = createFeedback.execute(feedbackCreateRequest, request);

        //Then
        assertNotNull(feedbackResponse);
        assertEquals("Title", feedbackResponse.getTitle());
        assertEquals("Description", feedbackResponse.getDescription());
        assertEquals("QUESTION", feedbackResponse.getCategory());
        assertEquals("NEW", feedbackResponse.getStatus());
    }

}
package br.com.feedhub.application.usecases.feedback;

import br.com.feedhub.interfaces.dto.request.feedback.FeedbackUpdateRequest;
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
class UpdateFeedbackTest {

    @Mock
    UpdateFeedback updateFeedback;

    @Mock
    private HttpServletRequest request;

    private FeedbackUpdateRequest feedbackUpdateRequest;
    private FeedbackResponse feedbackResponse;

    @BeforeEach
    public void setup() {
        feedbackUpdateRequest = new FeedbackUpdateRequest(
                "Title Updated",
                "Description Updated",
                "QUESTION",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L
        );

        feedbackResponse = new FeedbackResponse(
                "Title Updated",
                "Description Updated",
                "QUESTION",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                1L,
                "NEW"
        );
    }

    @Test
    @DisplayName("Test receive FeedbackCreateRequest when update then return FeedbackResponse")
    void testReceiveFeedbackUpdateRequest_whenUpdate_thenReturnFeedbackResponse() {
        //Given
        given(updateFeedback.execute(any(), any())).willReturn(feedbackResponse);

        //When
        FeedbackResponse feedbackResponse = updateFeedback.execute(feedbackUpdateRequest, request);

        //Then
        assertNotNull(feedbackResponse);
        assertEquals(feedbackUpdateRequest.getTitle(), feedbackResponse.getTitle());
        assertEquals(feedbackUpdateRequest.getDescription(), feedbackResponse.getDescription());
        assertEquals(feedbackUpdateRequest.getCategory(), feedbackResponse.getCategory());
    }

}
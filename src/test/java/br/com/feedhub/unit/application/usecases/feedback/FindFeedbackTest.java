package br.com.feedhub.unit.application.usecases.feedback;

import br.com.feedhub.application.usecases.feedback.FindFeedback;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class FindFeedbackTest {

    @Mock
    FindFeedback findFeedback;

    private FeedbackResponse feedbackResponse;

    @BeforeEach
    public void setup() {
        //Given
        feedbackResponse = new FeedbackResponse(
                "DVD com problema",
                "O aparelho não está lendo o dvd",
                "BUG",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                1L,
                "NEW"
        );

    }

    @Test
    @DisplayName("Test Given Feedback Object when findById Feedback then Return Saved Feedback")
    void testReceiveLongId_whenFindUser_thenReturnUserResponse() {
        //Given
        given(findFeedback.execute(anyLong(), any())).willReturn(feedbackResponse);

        //When
        FeedbackResponse response = findFeedback.execute(anyLong(), any());

        //Then
        assertNotNull(response);
        assertEquals(feedbackResponse.getId(), response.getId());
        assertEquals(feedbackResponse.getTitle(), response.getTitle());
        assertEquals(feedbackResponse.getDescription(), response.getDescription());
        assertEquals(feedbackResponse.getCategory(), response.getCategory());
    }

}

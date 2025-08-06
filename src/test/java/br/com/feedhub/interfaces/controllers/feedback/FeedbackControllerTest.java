package br.com.feedhub.interfaces.controllers.feedback;

import br.com.feedhub.application.usecases.feedback.CreateFeedback;
import br.com.feedhub.application.usecases.feedback.ListUserFeedback;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackCreateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    CreateFeedback createFeedback;

    @Mock
    ListUserFeedback listUserFeedback;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    FeedbackController feedbackController;

    private FeedbackCreateRequest feedbackRequest;
    private FeedbackResponse feedbackResponse;

    @BeforeEach
    public void setup() {
        feedbackRequest = new FeedbackCreateRequest("Bug", "Nao esta funcionado", "BUG", LocalDateTime.now(), LocalDateTime.now());
        feedbackResponse = new FeedbackResponse(
                feedbackRequest.getTitle(),
                feedbackRequest.getDescription(),
                feedbackRequest.getCategory(),
                feedbackRequest.getDateCreated(),
                feedbackRequest.getLastUpdated(),
                1L,
                1L,
                "NEW"
        );
    }

    @Test
    @DisplayName("Test Receive UserFeedbackCreateRequest when Create Feedback then Return FeedbackResponse")
    void testReceiveUserFeedbackCreateRequest_whenCreateFeedback_thenReturnFeedbackResponse() {
        //Given
        given(createFeedback.execute(any(FeedbackCreateRequest.class), any(HttpServletRequest.class)))
                .willReturn(feedbackResponse);
        //When
        ResponseEntity<?> responseEntity = feedbackController.create(feedbackRequest, request);
        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(feedbackResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test Receive Params when List then Response then Return PageListResponse")
    void testReceiveParam_whenList_thenResponse_thenReturnPageListResponse() {
        //Given
        var expectedPage = new PageListResponse<>(List.of(feedbackResponse),0, 10, 1, 1, true);
        given(listUserFeedback.execute(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString(), anyString(), any(HttpServletRequest.class)))
                .willReturn(expectedPage);

        //When
        ResponseEntity<?> responseEntity = feedbackController.list(
                "Bug",
                "Nao esta funcionado",
                "BUG",
                "NEW",
                0,
                10,
                "title",
                "asc",
                request
        );
        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPage, responseEntity.getBody());
    }


}
package br.com.feedhub.application.usecases.feedback;

import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ListUserFeedbackTest {

    @Mock
    ListUserFeedback listUserFeedback;

    @Mock
    HttpServletRequest request;

    PageListResponse<FeedbackResponse> pageListResponse;

    @BeforeEach
    public void setup() {
        //Given
        FeedbackResponse feedbackResponse = new FeedbackResponse(
                "Title Updated",
                "Description Updated",
                "QUESTION",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                1L,
                "NEW"
        );

        pageListResponse = new PageListResponse<>(
                List.of(feedbackResponse),
                0,
                10,
                1L,
                1,
                true
        );

    }

    @Test
    @DisplayName("Test receive params when ListUserFeedback then return PageListResponse")
    void testReceiveParams_whenListUserFeedback_thenReturnPageListResponse() {
        //Given
        given(listUserFeedback.execute(
                "Title",
                "Description",
                "QUESTION",
                "NEW",
                0,
                10,
                "title",
                "asc",
                request
        )).willReturn(pageListResponse);

        //When
        PageListResponse<FeedbackResponse> response = listUserFeedback.execute(
                "Title",
                "Description",
                "QUESTION",
                "NEW",
                0,
                10,
                "title",
                "asc",
                request
        );

        //Then
        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(1, response.totalElements());
        assertEquals(10, response.size());
    }

}
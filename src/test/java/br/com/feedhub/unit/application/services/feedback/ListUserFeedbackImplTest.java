package br.com.feedhub.unit.application.services.feedback;

import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.services.feedback.ListUserFeedbackImpl;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ListUserFeedbackImplTest {

    @InjectMocks
    ListUserFeedbackImpl listUserFeedback;

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
    private Pageable pageable;
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

        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "title");
        pageable = PageRequest.of(0, 10, sort);

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
        bearerToken =  "Bearer " + token;
    }

    @Test
    @DisplayName("Test receive params when ListUserFeedback then return PageListResponse")
    void testReceiveParams_whenListUserFeedback_thenReturnPageListResponse() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        List<Feedback> feedbacks = List.of(feedback);
        Page<Feedback> expectedPage = new PageImpl<>(feedbacks, pageable, feedbacks.size());
        given(feedbackGateway.findAllByFilters(
                user,
                "Title",
                "Description",
                "QUESTION",
                "NEW",
                pageable
        )).willReturn(expectedPage);

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

    @Test
    @DisplayName("Test receive params when ListUserFeedback throw Exception when User is null")
    void testReceiveParams_whenListUserFeedback_thenThrowExceptionWhenUserIsNull() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(userGateway.findByUsername(anyString())).willReturn(Optional.empty());

        //When
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> listUserFeedback.execute(
                        "Title",
                        "Description",
                        "QUESTION",
                        "NEW",
                        0,
                        10,
                        "title",
                        "desc",
                        request
                )
        );

        //Then
        assertEquals("Author with id: user-test not found", exception.getMessage());

    }
}
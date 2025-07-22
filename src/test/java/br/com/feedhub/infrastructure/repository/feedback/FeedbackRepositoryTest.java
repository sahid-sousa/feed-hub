package br.com.feedhub.infrastructure.repository.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.security.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FeedbackRepositoryTest {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    UserRepository userRepository;

    private User user;
    private Feedback feedback;

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        feedback = new Feedback();
        feedback.setUser(user);
        feedback.setTitle("Title");
        feedback.setDescription("Description");
        feedback.setCategory(FeedbackCategory.QUESTION);
        feedback.setStatus(FeedbackStatus.NEW);
    }

    @Test
    @DisplayName("Test Given Feedback Object when Save Feedback then Return Saved Feedback")
    void testGivenFeedbackObject_whenSaveFeedback_theReturnSavedFeedback() {
        //When
        userRepository.save(user);
        Feedback savedFeedback = feedbackRepository.save(feedback);

        //Then
        assertNotNull(savedFeedback);
        assertEquals("User", savedFeedback.getUser().getName());
        assertEquals("user-test", savedFeedback.getUser().getUsername());
        assertEquals("123456", savedFeedback.getUser().getPassword());
        assertEquals("email@email.com", savedFeedback.getUser().getEmail());

        assertEquals("Title", savedFeedback.getTitle());
        assertEquals("Description", savedFeedback.getDescription());
    }

    @Test
    @DisplayName("Test Given ListFeedback Object when findAllByUser then Return Saved Feedback")
    void testGivenListFeedbackObject_whenfindAllByUser_theReturnListFeedback() {
        //When
        userRepository.save(user);
        feedbackRepository.save(feedback);
        Optional<List<Feedback>> feedbacks = feedbackRepository.findAllByUser(user, PageRequest.of(0, 10));

        //Then
        assertTrue(feedbacks.isPresent());
        assertEquals("User", feedbacks.get().getFirst().getUser().getName());
        assertEquals("user-test", feedbacks.get().getFirst().getUser().getUsername());
        assertEquals("123456", feedbacks.get().getFirst().getUser().getPassword());
        assertEquals("email@email.com", feedbacks.get().getFirst().getUser().getEmail());

        assertEquals("Title", feedbacks.get().getFirst().getTitle());
        assertEquals("Description", feedbacks.get().getFirst().getDescription());
    }


}
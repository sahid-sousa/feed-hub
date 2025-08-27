package br.com.feedhub.unit.infrastructure.repository.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.feedback.FeedbackRepository;
import br.com.feedhub.infrastructure.repository.security.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    private Pageable pageable;

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
    }

    @Test
    @DisplayName("Test Given Feedback Object when Save Feedback then Return Saved Feedback")
    void testGivenFeedbackObject_whenSaveFeedback_theReturnSavedFeedback() {
        //When
        userRepository.save(user);
        Feedback savedFeedback = feedbackRepository.save(feedback);

        //Then
        assertNotNull(savedFeedback);
        assertEquals("User", savedFeedback.getAuthor().getName());
        assertEquals("user-test", savedFeedback.getAuthor().getUsername());
        assertEquals("123456", savedFeedback.getAuthor().getPassword());
        assertEquals("email@email.com", savedFeedback.getAuthor().getEmail());
        assertEquals("Title", savedFeedback.getTitle());
        assertEquals("Description", savedFeedback.getDescription());
    }

    @Test
    @DisplayName("Test Given ListFeedback Object when findAllByUserAuthor then Return Saved Feedback")
    void testGivenListFeedbackObject_whenfindAllByUserAuthor_theReturnListFeedback() {
        //When
        userRepository.save(user);
        feedbackRepository.save(feedback);
        Optional<List<Feedback>> feedbacks = feedbackRepository.findAllByAuthor(user, PageRequest.of(0, 10));

        //Then
        assertTrue(feedbacks.isPresent());
        assertEquals("User", feedbacks.get().getFirst().getAuthor().getName());
        assertEquals("user-test", feedbacks.get().getFirst().getAuthor().getUsername());
        assertEquals("123456", feedbacks.get().getFirst().getAuthor().getPassword());
        assertEquals("email@email.com", feedbacks.get().getFirst().getAuthor().getEmail());
        assertEquals("Title", feedbacks.get().getFirst().getTitle());
        assertEquals("Description", feedbacks.get().getFirst().getDescription());
    }

    @Test
    @DisplayName("Test Given ListFeedback Object when findAllByFilters then Return Saved Feedback")
    void testGivenListFeedbackObject_whenfindAllByFilters_theReturnListFeedback() {
        //When
        userRepository.save(user);
        feedbackRepository.save(feedback);
        Page<Feedback> feedbacksPage = feedbackRepository.findAllByFilters(user, "Title", "Description", "QUESTION", "NEW", pageable);


        //Then
        assertTrue(feedbacksPage.hasContent());
        assertTrue(feedbacksPage.getTotalElements() > 0);
        assertTrue(feedbacksPage.getNumberOfElements() > 0);
        feedbacksPage.forEach(Assertions::assertNotNull);
    }


}
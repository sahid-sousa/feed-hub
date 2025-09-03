package br.com.feedhub.unit.infrastructure.repository.comment;

import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.comment.CommentRepository;
import br.com.feedhub.infrastructure.repository.feedback.FeedbackRepository;
import br.com.feedhub.infrastructure.repository.security.UserRepository;
import br.com.feedhub.integration.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    private Comment comment;
    private User author;
    private Feedback feedback;

    @BeforeEach
    public void setup() {
        author = new User();
        author.setName("Author");
        author.setUsername("user-author");
        author.setPassword("123456");
        author.setEmail("email@email.com");

        feedback = new Feedback();
        feedback.setAuthor(author);
        feedback.setTitle("Title");
        feedback.setDescription("Description");
        feedback.setCategory(FeedbackCategory.QUESTION);
        feedback.setStatus(FeedbackStatus.NEW);

        comment = new Comment();
        comment.setAuthor(author);
        comment.setFeedback(feedback);
        comment.setContent("content-comment");
        comment.setDateCreated(LocalDateTime.now());
        comment.setLastUpdated(LocalDateTime.now());
    }

    @Test
    @DisplayName("Test Given Comment Object when Save Comment then Return Saved Comment")
    void testGivenCommentObject_whenSaveComment_theReturnSavedComment() {
        //When
        userRepository.save(author);
        feedbackRepository.save(feedback);
        Comment savedComment = commentRepository.save(comment);

        //Then
        assertNotNull(savedComment);
        assertEquals("content-comment", savedComment.getContent());

        assertEquals("Author", savedComment.getAuthor().getName());
        assertEquals("user-author", savedComment.getAuthor().getUsername());
        assertEquals("123456", savedComment.getAuthor().getPassword());
        assertEquals("email@email.com", savedComment.getAuthor().getEmail());

        assertEquals("Title", savedComment.getFeedback().getTitle());
        assertEquals("Description", savedComment.getFeedback().getDescription());
    }

    @Test
    @DisplayName("Test given CommentObject when FindById then Return Comment")
    void testGivenCommentObject_whenFindById_theReturnComment() {
        //Given
        userRepository.save(author);
        feedbackRepository.save(feedback);
        commentRepository.save(comment);

        //When
        Optional<Comment> optionalComment = commentRepository.findById(comment.getId());

        //Then
        assertTrue(optionalComment.isPresent());
        assertEquals("Author", optionalComment.get().getAuthor().getName());
        assertEquals("user-author", optionalComment.get().getAuthor().getUsername());
        assertEquals("123456", optionalComment.get().getAuthor().getPassword());
        assertEquals("email@email.com", optionalComment.get().getAuthor().getEmail());

        assertEquals("Title", optionalComment.get().getFeedback().getTitle());
        assertEquals("Description", optionalComment.get().getFeedback().getDescription());
    }

    @Test
    @DisplayName("Test given CommentObject when FindByIdAndAuthor then Return Comment")
    void testGivenCommentObject_whenFindByIdAndAuthor_theReturnComment() {
        //Given
        userRepository.save(author);
        feedbackRepository.save(feedback);
        commentRepository.save(comment);

        //When
        Optional<Comment> optionalComment = commentRepository.findByIdAndAuthor(comment.getId(), author);

        //Then
        assertTrue(optionalComment.isPresent());
        assertEquals("Author", optionalComment.get().getAuthor().getName());
        assertEquals("user-author", optionalComment.get().getAuthor().getUsername());
        assertEquals("123456", optionalComment.get().getAuthor().getPassword());
        assertEquals("email@email.com", optionalComment.get().getAuthor().getEmail());

        assertEquals("Title", optionalComment.get().getFeedback().getTitle());
        assertEquals("Description", optionalComment.get().getFeedback().getDescription());
    }

    @Test
    @DisplayName("Test Given Comments Object when findAllByFeedback then Return Comments")
    void testGivenCommentsObject_whenFindAllByFeedback_theReturnComments() {
        //When
        userRepository.save(author);
        feedbackRepository.save(feedback);
        commentRepository.save(comment);

        Optional<List<Comment>> comments = commentRepository.findAllByFeedback(feedback, PageRequest.of(0, 10));

        //Then
        assertTrue(comments.isPresent());
        assertFalse(comments.get().isEmpty());
        assertEquals("Author", comments.get().getFirst().getAuthor().getName());
        assertEquals("user-author", comments.get().getFirst().getAuthor().getUsername());
        assertEquals("123456", comments.get().getFirst().getAuthor().getPassword());
        assertEquals("email@email.com", comments.get().getFirst().getAuthor().getEmail());

        assertEquals("Title", comments.get().getFirst().getFeedback().getTitle());
        assertEquals("Description", comments.get().getFirst().getFeedback().getDescription());
    }

    @Test
    @DisplayName("Test Given Comments Object when findAllByAuthor then Return Comments")
    void testGivenCommentsObject_whenFindAllByAuthor_theReturnComments() {
        //Given
        userRepository.save(author);
        feedbackRepository.save(feedback);
        commentRepository.save(comment);

        //When
        Optional<List<Comment>> comments = commentRepository.findAllByAuthor(author, PageRequest.of(0, 10));

        //Then
        assertTrue(comments.isPresent());
        assertFalse(comments.get().isEmpty());
        assertEquals("Author", comments.get().getFirst().getAuthor().getName());
        assertEquals("user-author", comments.get().getFirst().getAuthor().getUsername());
        assertEquals("123456", comments.get().getFirst().getAuthor().getPassword());
        assertEquals("email@email.com", comments.get().getFirst().getAuthor().getEmail());

        assertEquals("Title", comments.get().getFirst().getFeedback().getTitle());
        assertEquals("Description", comments.get().getFirst().getFeedback().getDescription());
    }

    @Test
    @DisplayName("Test Given Comments Object when findAllByFilters then Return Comments")
    void testGivenCommentsObject_whenFindAllByFilters_theReturnComments() {
        //Given
        userRepository.save(author);
        feedbackRepository.save(feedback);
        commentRepository.save(comment);

        //When
        Page<Comment> commentsPage = commentRepository.findAllByFilters(feedback, PageRequest.of(0, 10));

        //Then
        assertTrue(commentsPage.hasContent());
        assertTrue(commentsPage.getTotalElements() > 0);
        assertTrue(commentsPage.getTotalPages() > 0);
        commentsPage.forEach(Assertions::assertNotNull);
    }

}
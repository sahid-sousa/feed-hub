package br.com.feedhub.unit.adapters.database.comment;

import br.com.feedhub.adapters.database.comment.CommentGatewayImpl;
import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.comment.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentGatewayImplTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentGatewayImpl commentGateway;

    private Comment comment;
    private Feedback feedback;
    private Pageable pageable;
    private User author;

    @BeforeEach
    public void setup() {
        //Given
        User user = new User();
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


        author = new User();
        author.setName("User Author");
        author.setUsername("user-author");
        author.setPassword("246810");
        author.setEmail("email@email.com");

        comment = new Comment();
        comment.setContent("Comentario do feedback");
        comment.setAuthor(author);
        comment.setFeedback(feedback);
        comment.setDateCreated(LocalDateTime.now());
        comment.setLastUpdated(LocalDateTime.now());

        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "title");
        pageable = PageRequest.of(0, 10, sort);
    }

    @Test
    @DisplayName("Test Given Comment Object when Save Comment then Return Saved Comment")
    void testGivenCommentObject_whenSaveComment_theReturnSavedComment() {
        //When
        given(commentRepository.save(comment)).willReturn(comment);
        Comment savedComment = commentGateway.save(comment);

        //Then
        assertNotNull(savedComment);
        assertEquals("Comentario do feedback", savedComment.getContent());
        assertEquals("User Author", savedComment.getAuthor().getName());
    }

    @Test
    @DisplayName("Test given CommentObject when FindById then Return Comment")
    void testGivenCommentObject_whenFindById_theReturnComment() {

    }

    @Test
    @DisplayName("Test given CommentObject when FindByIdAndAuthor then Return Comment")
    void testGivenCommentObject_whenFindByIdAndAuthor_theReturnComment() {

    }

    @Test
    @DisplayName("Test Given Comments Object when findAllByFeedback then Return Comments")
    void testGivenCommentsObject_whenFindAllByFeedback_theReturnComments() {
        //When
        Sort.Direction  direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "dateCreated");
        Pageable pageable = PageRequest.of(0, 10, sort);
        given(commentGateway.findAllByFeedback(feedback, pageable))
                .willReturn(Optional.of(List.of(comment)));
        Optional<List<Comment>> optionalCommentList = commentGateway.findAllByFeedback(feedback, pageable);

        //Then
        assertTrue(optionalCommentList.isPresent());
        assertFalse(optionalCommentList.get().isEmpty());
        assertEquals(1, optionalCommentList.get().size());
    }

    @Test
    @DisplayName("Test Given ListComment Object when findAllByAuthor then Return Saved Comments")
    void testGivenListCommentObject_whenfindAllByAuthor_theReturnListComments() {
        //When
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "dateCreated");
        Pageable pageable = PageRequest.of(0,10, sort);
        given(commentGateway.findAllByAuthor(author, pageable))
                .willReturn(Optional.of(List.of(comment)));
        Optional<List<Comment>> optionalCommentList = commentGateway.findAllByAuthor(author, pageable);

        //Then
        assertTrue(optionalCommentList.isPresent());
        assertFalse(optionalCommentList.get().isEmpty());
        assertEquals(1, optionalCommentList.get().size());
    }

    @Test
    @DisplayName("Test Given Comments Object when findAllByFilters then Return Comments")
    void testGivenCommentsObject_whenFindAllByFilters_theReturnComments() {
        //Given
        List<Comment> comments = List.of(comment);
        Page<Comment> expectedPage = new PageImpl<>(comments, pageable, comments.size());
        given(commentGateway.findAllByFilters(feedback, pageable)).willReturn(expectedPage);

        //When
        Page<Comment> commentsPage = commentGateway.findAllByFilters(feedback, pageable);

        //Then
        assertTrue(commentsPage.hasContent());
        assertTrue(commentsPage.getTotalElements() > 0);
        assertTrue(commentsPage.getNumberOfElements() > 0);
        commentsPage.forEach(Assertions::assertNotNull);
    }

}
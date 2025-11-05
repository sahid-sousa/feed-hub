package br.com.feedhub.adapters.database.comment;

import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.comment.CommentMonthCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentGateway {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    Optional<Comment> findByIdAndAuthor(Long id, User author);
    Optional<List<Comment>> findAllByFeedback(Feedback feedback, Pageable pageable);
    Optional<List<Comment>> findAllByAuthor(User author, Pageable pageable);
    Integer countByAuthor(User author);
    List<CommentMonthCount> findAllByUserAndGroupMonth(User author, Integer startMonth, Integer endMonth);
    Page<Comment> findAllByFilters(Feedback feedback, Pageable pageable);

}

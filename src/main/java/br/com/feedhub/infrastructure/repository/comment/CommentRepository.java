package br.com.feedhub.infrastructure.repository.comment;

import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends Repository<Comment, Long> {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    Optional<Comment> findByIdAndAuthor(Long id, User author);
    Optional<List<Comment>> findAllByFeedback(Feedback feedback, Pageable pageable);
    Optional<List<Comment>> findAllByAuthor(User author, Pageable pageable);
}

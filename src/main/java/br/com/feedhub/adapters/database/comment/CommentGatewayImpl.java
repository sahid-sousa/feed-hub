package br.com.feedhub.adapters.database.comment;

import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.comment.CommentMonthCount;
import br.com.feedhub.infrastructure.repository.comment.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentGatewayImpl implements CommentGateway {

    CommentRepository commentRepository;

    public CommentGatewayImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Optional<Comment> findByIdAndAuthor(Long id, User author) {
        return commentRepository.findByIdAndAuthor(id, author);
    }

    @Override
    public Optional<List<Comment>> findAllByFeedback(Feedback feedback, Pageable pageable) {
        return commentRepository.findAllByFeedback(feedback, pageable);
    }

    @Override
    public Optional<List<Comment>> findAllByAuthor(User author, Pageable pageable) {
        return commentRepository.findAllByAuthor(author, pageable);
    }

    @Override
    public Integer countByAuthor(User author) {
        return commentRepository.countByAuthor(author);
    }

    @Override
    public List<CommentMonthCount> findAllByUserAndGroupMonth(User author, Integer startMonth, Integer endMonth) {
        return commentRepository.findAllByUserAndGroupMonth(author, startMonth, endMonth);
    }

    @Override
    public Page<Comment> findAllByFilters(Feedback feedback, Pageable pageable) {
        return commentRepository.findAllByFilters(feedback, pageable);
    }
}

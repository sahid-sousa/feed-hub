package br.com.feedhub.application.services.comment;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.comment.CreateComment;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.comment.CommentCreateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CreateCommentImpl implements CreateComment {

    private final CommentGateway commentGateway;
    private final ExtractUsername extractUsername;
    private final FeedbackGateway feedbackGateway;
    private final UserGateway userGateway;

    public CreateCommentImpl(CommentGateway commentGateway, ExtractUsername extractUsername, FeedbackGateway feedbackGateway, UserGateway userGateway) {
        this.commentGateway = commentGateway;
        this.extractUsername = extractUsername;
        this.feedbackGateway = feedbackGateway;
        this.userGateway = userGateway;
    }

    @Override
    public CommentResponse execute(CommentCreateRequest commentCreateRequest, HttpServletRequest request) {
        String username = extractUsername.execute(request.getHeader("Authorization"));
        Optional<User> author = userGateway.findByUsername(username);
        if (author.isEmpty()) {
            throw new RequiredObjectIsNullException("User not found with " + username);
        }
        Comment comment = createComment(author.get(), commentCreateRequest);
        return GenericBuilder.of(CommentResponse::new)
                .with(CommentResponse::setId, comment.getId())
                .with(CommentResponse::setAuthorId, author.get().getId())
                .with(CommentResponse::setFeedbackId, comment.getFeedback().getId())
                .with(CommentResponse::setContent, comment.getContent())
                .with(CommentResponse::setDateCreated, comment.getDateCreated())
                .with(CommentResponse::setLastUpdated, comment.getLastUpdated())
                .build();
    }

    public Comment createComment(User author, CommentCreateRequest commentCreateRequest) {
        Optional<Feedback> feedback = feedbackGateway.findById(commentCreateRequest.getFeedbackId());
        if (feedback.isEmpty()) {
            throw new RequiredObjectIsNullException("Feedback not found with id " + commentCreateRequest.getFeedbackId());
        }
        Comment comment = GenericBuilder.of(Comment::new)
                .with(Comment::setAuthor, author)
                .with(Comment::setContent, commentCreateRequest.getContent())
                .with(Comment::setFeedback, feedback.get())
                .with(Comment::setDateCreated, LocalDateTime.now())
                .with(Comment::setLastUpdated, LocalDateTime.now())
                .build();
        return commentGateway.save(comment);
    }

}
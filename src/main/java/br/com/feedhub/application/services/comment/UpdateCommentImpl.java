package br.com.feedhub.application.services.comment;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.comment.UpdateComment;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.comment.CommentUpdateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UpdateCommentImpl implements UpdateComment {

    private final CommentGateway commentGateway;
    private final ExtractUsername extractUsername;
    private final UserGateway userGateway;

    public UpdateCommentImpl(CommentGateway commentGateway, ExtractUsername extractUsername, UserGateway userGateway) {
        this.commentGateway = commentGateway;
        this.extractUsername = extractUsername;
        this.userGateway = userGateway;
    }

    @Override
    public CommentResponse execute(CommentUpdateRequest commentUpdateRequest, HttpServletRequest request) {
        String username = extractUsername.execute(request.getHeader("Authorization"));
        Optional<User> author = userGateway.findByUsername(username);
        if (author.isEmpty()) {
            throw new RequiredObjectIsNullException("User not found with " + username);
        }
        Optional<Comment> comment = commentGateway.findByIdAndAuthor(commentUpdateRequest.getId(), author.get());
        if (comment.isEmpty()) {
            throw new RequiredObjectIsNullException("Comment not found with " + commentUpdateRequest.getId());
        }
        Comment updatedComment = updateComment(comment.get(), commentUpdateRequest);
        return GenericBuilder.of(CommentResponse::new)
                .with(CommentResponse::setId, comment.get().getId())
                .with(CommentResponse::setContent, updatedComment.getContent())
                .with(CommentResponse::setAuthorId, comment.get().getAuthor().getId())
                .with(CommentResponse::setFeedbackId, comment.get().getFeedback().getId())
                .with(CommentResponse::setDateCreated, comment.get().getDateCreated())
                .with(CommentResponse::setLastUpdated, comment.get().getLastUpdated())
                .build();
    }

    public Comment updateComment(Comment comment, CommentUpdateRequest commentUpdateRequest) {
        return commentGateway.save(
                GenericBuilder.of(comment)
                        .with(Comment::setContent, commentUpdateRequest.getContent())
                        .with(Comment::setLastUpdated, LocalDateTime.now())
                        .build()
        );
    }
}

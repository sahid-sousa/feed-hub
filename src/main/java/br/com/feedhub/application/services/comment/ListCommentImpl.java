package br.com.feedhub.application.services.comment;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.comment.ListComment;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListCommentImpl implements ListComment {

    CommentGateway commentGateway;
    ExtractUsername extractUsername;
    FeedbackGateway feedbackGateway;
    UserGateway userGateway;

    public ListCommentImpl(CommentGateway commentGateway, ExtractUsername extractUsername, FeedbackGateway feedbackGateway, UserGateway userGateway) {
        this.commentGateway = commentGateway;
        this.extractUsername = extractUsername;
        this.feedbackGateway = feedbackGateway;
        this.userGateway = userGateway;
    }

    @Override
    public PageListResponse<CommentResponse> execute(Long feedbackId, int page, int size, String sortBy, String sortDirection, HttpServletRequest request) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Optional<Feedback> feedback = feedbackGateway.findById(feedbackId);
        if (feedback.isEmpty()) {
            throw new RequiredObjectIsNullException("Feedback object with id " + feedbackId + " is null");
        }
        String username = extractUsername.execute(request.getHeader("Authorization"));
        Optional<User> user = userGateway.findByUsername(username);
        if (user.isEmpty()) {
            throw new RequiredObjectIsNullException("User object with username " + username + " is null");
        }
        Page<Comment> commentsPage = commentGateway.findAllByFilters(feedback.get(), pageable);
        Page<CommentResponse> responsePage = commentsPage.map( comment ->
                GenericBuilder.of(CommentResponse::new)
                        .with(CommentResponse::setContent, comment.getContent())
                        .with(CommentResponse::setId, comment.getId())
                        .with(CommentResponse::setFeedbackId, comment.getFeedback().getId())
                        .with(CommentResponse::setAuthorId, comment.getAuthor().getId())
                        .with(CommentResponse::setDateCreated, comment.getDateCreated())
                        .with(CommentResponse::setLastUpdated, comment.getLastUpdated())
                        .build()
        );
        return PageListResponse.of(responsePage);
    }
}

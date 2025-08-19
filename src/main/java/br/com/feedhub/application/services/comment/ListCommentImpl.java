package br.com.feedhub.application.services.comment;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.application.usecases.comment.ListComment;
import br.com.feedhub.domain.comment.Comment;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListCommentImpl implements ListComment {

    CommentGateway commentGateway;
    FeedbackGateway feedbackGateway;


    @Override
    public PageListResponse<CommentResponse> execute(Long feedbackId, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Optional<Feedback> feedback = feedbackGateway.findById(feedbackId);
        if (feedback.isEmpty()) {
            throw new RequiredObjectIsNullException("Feedback object with id " + feedbackId + " is null");
        }
        Page<Comment> commentsPage = commentGateway.findAllByFilters(feedback.get(), pageable);
        Page<CommentResponse> responsePage = commentsPage.map( comment ->
                GenericBuilder.of(CommentResponse::new).build()
        );
        return PageListResponse.of(responsePage);
    }
}

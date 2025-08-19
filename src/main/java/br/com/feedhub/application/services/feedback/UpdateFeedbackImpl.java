package br.com.feedhub.application.services.feedback;

import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.feedback.UpdateFeedback;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackUpdateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UpdateFeedbackImpl implements UpdateFeedback {

    private final ExtractUsername extractUsername;
    private final FeedbackGateway feedbackGateway;
    private final UserGateway userGateway;

    public UpdateFeedbackImpl(ExtractUsername extractUsername, FeedbackGateway feedbackGateway, UserGateway userGateway) {
        this.extractUsername = extractUsername;
        this.feedbackGateway = feedbackGateway;
        this.userGateway = userGateway;
    }

    @Override
    public FeedbackResponse execute(FeedbackUpdateRequest feedbackUpdateRequest, HttpServletRequest request) {
        String username = extractUsername.execute(request.getHeader("Authorization"));
        Optional<User> optionalUser = userGateway.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new RequiredObjectIsNullException("Username " + username + " not exists");
        }
        Optional<Feedback> optionalFeedback = feedbackGateway.findByIdAndAuthor(feedbackUpdateRequest.getId(), optionalUser.get());
        if (optionalFeedback.isEmpty()) {
            throw new RequiredObjectIsNullException("Feedback with id " + feedbackUpdateRequest.getId() + " not exists");
        }
        Feedback feedback = GenericBuilder.of(optionalFeedback.get())
                .with(Feedback::setTitle, feedbackUpdateRequest.getTitle())
                .with(Feedback::setDescription, feedbackUpdateRequest.getDescription())
                .with(Feedback::setCategory, FeedbackCategory.valueOf(feedbackUpdateRequest.getCategory()))
                .with(Feedback::setLastUpdated, LocalDateTime.now())
                .build();
        feedbackGateway.save(feedback);
        return GenericBuilder.of(FeedbackResponse::new)
                .with(FeedbackResponse::setId, feedback.getId())
                .with(FeedbackResponse::setTitle, feedback.getTitle())
                .with(FeedbackResponse::setDescription, feedback.getDescription())
                .with(FeedbackResponse::setCategory, feedback.getCategory().name())
                .with(FeedbackResponse::setDateCreated, feedback.getDateCreated())
                .with(FeedbackResponse::setLastUpdated, feedback.getLastUpdated())
                .build();
    }
}

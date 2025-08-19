package br.com.feedhub.application.services.feedback;

import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.feedback.CreateFeedback;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.feedback.FeedbackCategory;
import br.com.feedhub.domain.feedback.FeedbackStatus;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackCreateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateFeedbackImpl implements CreateFeedback {

    private final ExtractUsername extractUsername;
    private final UserGateway userGateway;
    private final FeedbackGateway feedbackGateway;

    public CreateFeedbackImpl(ExtractUsername extractUsername, UserGateway userGateway, FeedbackGateway feedbackGateway) {
        this.extractUsername = extractUsername;
        this.userGateway = userGateway;
        this.feedbackGateway = feedbackGateway;
    }

    @Override
    public FeedbackResponse execute(FeedbackCreateRequest feedbackCreateRequest, HttpServletRequest request) {
        String username = extractUsername.execute(request.getHeader("Authorization"));
        Optional<User> author = userGateway.findByUsername(username);
        if (author.isEmpty()) {
            throw new RequiredObjectIsNullException("Author with username " + username + " not found");
        }
        Feedback feedback = createFeedback(author.get(), feedbackCreateRequest);
        return GenericBuilder.of(FeedbackResponse::new)
                .with(FeedbackResponse::setId, feedback.getId())
                .with(FeedbackResponse::setAuthorId, feedback.getAuthor().getId())
                .with(FeedbackResponse::setTitle, feedback.getTitle())
                .with(FeedbackResponse::setDescription, feedback.getDescription())
                .with(FeedbackResponse::setCategory, feedback.getCategory().name())
                .with(FeedbackResponse::setStatus, feedback.getStatus().name())
                .with(FeedbackResponse::setLastUpdated, feedback.getLastUpdated())
                .with(FeedbackResponse::setDateCreated, feedback.getDateCreated())
                .build();
    }

    public Feedback createFeedback(User author, FeedbackCreateRequest feedbackCreateRequest) {
        Feedback feedback = GenericBuilder.of(Feedback::new)
                .with(Feedback::setTitle, feedbackCreateRequest.getTitle())
                .with(Feedback::setAuthor, author)
                .with(Feedback::setDescription, feedbackCreateRequest.getDescription())
                .with(Feedback::setCategory, FeedbackCategory.valueOf(feedbackCreateRequest.getCategory()))
                .with(Feedback::setStatus, FeedbackStatus.NEW)
                .build();
        return feedbackGateway.save(feedback);
    }

}

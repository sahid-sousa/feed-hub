package br.com.feedhub.application.services.feedback;

import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.feedback.FindFeedback;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindFeedbackImpl implements FindFeedback {

    private final FeedbackGateway feedbackGateway;
    private final ExtractUsername extractUsername;
    private final UserGateway userGateway;

    public FindFeedbackImpl(FeedbackGateway feedbackGateway, ExtractUsername extractUsername, UserGateway userGateway) {
        this.feedbackGateway = feedbackGateway;
        this.extractUsername = extractUsername;
        this.userGateway = userGateway;
    }

    @Override
    public FeedbackResponse execute(Long id, HttpServletRequest request) {
        String username = extractUsername.execute(request.getHeader("Authorization"));
        Optional<User> author = userGateway.findByUsername(username);
        if (author.isEmpty()) {
            throw new RequiredObjectIsNullException("Author with id: " + username + " not found");
        }
        Optional<Feedback> findedFeedback = feedbackGateway.findById(id);
        if (findedFeedback.isEmpty()) {
            throw new RequiredObjectIsNullException("Feedback not found for id " + id);
        }
        Feedback feedback = findedFeedback.get();
        return GenericBuilder.of(FeedbackResponse::new)
                .with(FeedbackResponse::setId, feedback.getId())
                .with(FeedbackResponse::setTitle, feedback.getTitle())
                .with(FeedbackResponse::setDescription, feedback.getDescription())
                .with(FeedbackResponse::setCategory, feedback.getCategory().name())
                .with(FeedbackResponse::setStatus, feedback.getStatus().name())
                .with(FeedbackResponse::setAuthorId, author.get().getId())
                .with(FeedbackResponse::setDateCreated, feedback.getDateCreated())
                .with(FeedbackResponse::setLastUpdated, feedback.getLastUpdated())
                .build();
    }

}

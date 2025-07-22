package br.com.feedhub.adapters.database.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FeedbackGateway {
    Feedback save(Feedback feedback);
    Optional<List<Feedback>> findAllByUser(User user, Pageable pageable);
}

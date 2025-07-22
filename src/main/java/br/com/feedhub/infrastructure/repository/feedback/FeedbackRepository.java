package br.com.feedhub.infrastructure.repository.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends Repository<Feedback, Long> {
    Feedback save(Feedback feedback);
    Optional<List<Feedback>> findAllByUser(User user, Pageable pageable);
}

package br.com.feedhub.adapters.database.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FeedbackGateway {
    Feedback save(Feedback feedback);
    Optional<Feedback> findByIdAndAuthor(Long id, User author);
    Optional<List<Feedback>> findAllByAuthor(User author, Pageable pageable);
    Page<Feedback> findAllByFilters(
            String title,
            String description,
            String category,
            String status,
            Pageable pageable
    );
}

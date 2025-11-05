package br.com.feedhub.adapters.database.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.feedback.FeedbackMonthCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FeedbackGateway {
    Feedback save(Feedback feedback);
    Optional<Feedback> findById(Long id);
    Optional<Feedback> findByIdAndAuthor(Long id, User author);
    Optional<List<Feedback>> findAllByAuthor(User author, Pageable pageable);
    Integer countByAuthor(User author);
    List<FeedbackMonthCount> findAllByUserAndGroupMonth(User author, Integer startMonth, Integer endMonth);
    Page<Feedback> findAllByFilters(
            User author,
            String title,
            String description,
            String category,
            String status,
            Pageable pageable
    );
}

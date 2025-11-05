package br.com.feedhub.adapters.database.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.feedback.FeedbackMonthCount;
import br.com.feedhub.infrastructure.repository.feedback.FeedbackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackGatewayImpl implements FeedbackGateway {

    private final FeedbackRepository feedbackRepository;

    public FeedbackGatewayImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Optional<Feedback> findByIdAndAuthor(Long id, User author) {
        return feedbackRepository.findByIdAndAuthor(id, author);
    }

    @Override
    public Optional<List<Feedback>> findAllByAuthor(User author, Pageable pageable) {
        return feedbackRepository.findAllByAuthor(author, pageable);
    }

    @Override
    public Integer countByAuthor(User author) {
        return this.feedbackRepository.countByAuthor(author);
    }

    @Override
    public List<FeedbackMonthCount> findAllByUserAndGroupMonth(User author, Integer startMonth, Integer endMonth) {
        return feedbackRepository.findAllByUserAndGroupMonth(author, startMonth, endMonth);
    }

    @Override
    public Page<Feedback> findAllByFilters(User author, String title, String description, String category, String status, Pageable pageable) {
        return feedbackRepository.findAllByFilters(author, title, description, category, status, pageable);
    }
}

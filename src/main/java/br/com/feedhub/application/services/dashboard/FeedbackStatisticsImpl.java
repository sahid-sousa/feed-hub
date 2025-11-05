package br.com.feedhub.application.services.dashboard;

import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.application.usecases.dashboard.FeedbackStatistics;
import br.com.feedhub.domain.security.User;
import org.springframework.stereotype.Service;

@Service
public class FeedbackStatisticsImpl implements FeedbackStatistics {

    private final FeedbackGateway feedbackGateway;

    public FeedbackStatisticsImpl(FeedbackGateway feedbackGateway) {
        this.feedbackGateway = feedbackGateway;
    }

    @Override
    public Integer execute(User author) {
        Integer count = feedbackGateway.countByAuthor(author);
        return count > 0 ? count : 0;
    }

}

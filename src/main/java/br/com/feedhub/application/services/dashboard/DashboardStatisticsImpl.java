package br.com.feedhub.application.services.dashboard;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.dashboard.CommentStatistics;
import br.com.feedhub.application.usecases.dashboard.DashboardStatistics;
import br.com.feedhub.application.usecases.dashboard.FeedbackStatistics;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.comment.CommentMonthCount;
import br.com.feedhub.infrastructure.repository.feedback.FeedbackMonthCount;
import br.com.feedhub.interfaces.dto.response.DashboardResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DashboardStatisticsImpl implements DashboardStatistics {

    private final CommentStatistics commentStatistics;
    private final ExtractUsername extractUsername;
    private final FeedbackStatistics feedbackStatistics;
    private final FeedbackGateway feedbackGateway;
    private final CommentGateway commentGateway;
    private final UserGateway userGateway;


    public DashboardStatisticsImpl(
            CommentStatistics commentStatistics, ExtractUsername extractUsername,
            FeedbackStatistics feedbackStatistics, FeedbackGateway feedbackGateway, CommentGateway commentGateway, UserGateway userGateway
    ) {
        this.commentStatistics = commentStatistics;
        this.extractUsername = extractUsername;
        this.feedbackStatistics = feedbackStatistics;
        this.feedbackGateway = feedbackGateway;
        this.commentGateway = commentGateway;
        this.userGateway = userGateway;
    }

    @Override
    public DashboardResponse execute(Integer startMonth, Integer endMonth, HttpServletRequest request) {
        String username = extractUsername.execute(request.getHeader("Authorization"));
        Optional<User> author = userGateway.findByUsername(username);
        if (author.isEmpty()) {
            throw new RequiredObjectIsNullException("User not found with " + username);
        }
        Integer countComments = commentStatistics.execute(author.get());
        Integer countFeedbacks = feedbackStatistics.execute(author.get());
        List<FeedbackMonthCount> feedbackMonthCounts =  feedbackGateway.findAllByUserAndGroupMonth(author.get(), startMonth, endMonth);
        List<CommentMonthCount> commentMonthCounts = commentGateway.findAllByUserAndGroupMonth(author.get(), startMonth, endMonth);
        return GenericBuilder.of(DashboardResponse::new)
                .with(DashboardResponse::setComments, countComments)
                .with(DashboardResponse::setFeedbacks, countFeedbacks)
                .with(DashboardResponse::setCommentMonthCounts, commentMonthCounts)
                .with(DashboardResponse::setFeedbackMonthCounts, feedbackMonthCounts)
                .build();
    }
}

package br.com.feedhub.application.services.dashboard;

import br.com.feedhub.adapters.database.comment.CommentGateway;
import br.com.feedhub.application.usecases.dashboard.CommentStatistics;
import br.com.feedhub.domain.security.User;
import org.springframework.stereotype.Service;

@Service
public class CommentStatisticsImpl implements CommentStatistics {

    private final CommentGateway commentGateway;

    public CommentStatisticsImpl(CommentGateway commentGateway) {
        this.commentGateway = commentGateway;
    }

    @Override
    public Integer execute(User author) {
        Integer count = commentGateway.countByAuthor(author);
        return count > 0 ? count : 0;
    }
}

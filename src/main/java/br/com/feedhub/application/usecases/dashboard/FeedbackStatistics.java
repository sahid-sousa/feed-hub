package br.com.feedhub.application.usecases.dashboard;

import br.com.feedhub.domain.security.User;

public interface FeedbackStatistics {
    Integer execute(User author);
}

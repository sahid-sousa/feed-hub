package br.com.feedhub.application.usecases.dashboard;

import br.com.feedhub.domain.security.User;

public interface CommentStatistics {
    Integer execute(User author);
}

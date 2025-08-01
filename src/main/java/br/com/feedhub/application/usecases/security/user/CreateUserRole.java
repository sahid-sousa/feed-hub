package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;

import java.util.List;

public interface CreateUserRole {
    void execute(User user, List<Role> roles );
}

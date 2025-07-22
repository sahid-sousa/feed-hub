package br.com.feedhub.adapters.database.user;

import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleGateway {
    UserRole save(UserRole userRole);
    void deleteAllByUser(User user);
    Optional<List<UserRole>> findAllByUser(User user);
}

package br.com.feedhub.infrastructure.repository.security;

import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends Repository<UserRole, Long> {
    UserRole save(UserRole userRole);
    void deleteAllByUser(User user);
    Optional<List<UserRole>> findAllByUser(User user);
}

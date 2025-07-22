package br.com.feedhub.infrastructure.repository.security;

import br.com.feedhub.domain.security.Role;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RoleRepository extends Repository<Role, Long> {
    Role save(Role role);
    Optional<Role> findById(Long id);
}

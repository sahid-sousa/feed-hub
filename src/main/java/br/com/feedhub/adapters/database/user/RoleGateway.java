package br.com.feedhub.adapters.database.user;

import br.com.feedhub.domain.security.Role;

import java.util.Optional;

public interface RoleGateway {
    Role save(Role role);
    Optional<Role> findById(Long id);
}

package br.com.feedhub.adapters.database.user;

import br.com.feedhub.domain.security.Role;
import br.com.feedhub.infrastructure.repository.security.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleGatewayImpl implements RoleGateway {

    private final RoleRepository roleRepository;

    public RoleGatewayImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
}

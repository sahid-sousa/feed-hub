package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.RoleGateway;
import br.com.feedhub.application.usecases.security.user.CreateRole;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.interfaces.dto.request.BaseUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateRoleImpl implements CreateRole {

    private final RoleGateway roleGateway;

    public CreateRoleImpl(RoleGateway roleGateway) {
        this.roleGateway = roleGateway;
    }

    @Override
    public List<Role> create(BaseUserDetails baseUserDetails) {
        return baseUserDetails.getAuthorities().stream()
                .map(authority -> {
                    Role role = new Role();
                    role.setName(authority.getAuthority().replace("ROLE_", ""));
                    role.setAuthority(authority.getAuthority());
                    return roleGateway.save(role);
                })
                .toList();
    }

}

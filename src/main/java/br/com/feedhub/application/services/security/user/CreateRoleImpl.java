package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.RoleGateway;
import br.com.feedhub.application.usecases.security.user.CreateRole;
import br.com.feedhub.domain.security.Role;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CreateRoleImpl implements CreateRole {

    private final RoleGateway roleGateway;

    public CreateRoleImpl(RoleGateway roleGateway) {
        this.roleGateway = roleGateway;
    }

    @Override
    public List<Role> execute(String[] roles) {
        return Arrays.stream(roles)
                .map(authority -> {
                    Role role = new Role();
                    role.setName(authority.replace("ROLE_", ""));
                    role.setAuthority(authority);
                    return roleGateway.save(role);
                })
                .toList();
    }

}

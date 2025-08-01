package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.usecases.security.user.CreateUserRole;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateUserRoleImpl implements CreateUserRole {

    private final UserRoleGateway userRoleGateway;

    public CreateUserRoleImpl(UserRoleGateway userRoleGateway) {
        this.userRoleGateway = userRoleGateway;
    }

    @Override
    public void execute(User user, List<Role> roles ) {
        roles.forEach(role -> {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleGateway.save(userRole);
        });
    }

}

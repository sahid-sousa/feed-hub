package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.RoleGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.usecases.security.user.FindUser;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.interfaces.dto.request.RoleDto;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FindUserImpl implements FindUser {

    private final UserGateway userGateway;
    private final RoleGateway roleGateway;
    private final UserRoleGateway userRoleGateway;

    public FindUserImpl(UserGateway userGateway, RoleGateway roleGateway, UserRoleGateway userRoleGateway) {
        this.userGateway = userGateway;
        this.roleGateway = roleGateway;
        this.userRoleGateway = userRoleGateway;
    }

    @Override
    public UserResponse find(Long id) {
        Optional<User> findedUser = userGateway.findById(id);
        if (findedUser.isEmpty()) {
            throw  new RequiredObjectIsNullException("User not found for id " + id);
        }
        User user = findedUser.get();
        List<RoleDto> userRoleList = getRoles(user);
        return GenericBuilder.of(UserResponse::new)
                .with(UserResponse::setId, user.getId())
                .with(UserResponse::setName,user.getName())
                .with(UserResponse::setUsername, user.getUsername())
                .with(UserResponse::setEmail, user.getEmail())
                .with(UserResponse::setPassword, "")
                .with(UserResponse::setAccountNonExpired, user.isAccountNonExpired())
                .with(UserResponse::setAccountNonLocked, user.isAccountNonLocked())
                .with(UserResponse::setCredentialsNonExpired, user.isCredentialsNonExpired())
                .with(UserResponse::setEnabled, user.isEnabled())
                .with(UserResponse::setAuthorities, userRoleList)
                .build();
    }

    List<RoleDto> getRoles(User user) {
        Optional<List<UserRole>> findedUserRoleList = userRoleGateway.findAllByUser(user);
        if (findedUserRoleList.isEmpty()) {
            throw  new RequiredObjectIsNullException("User not found for id " + user.getId());
        }
        List<RoleDto> userRoleList = new ArrayList<>();
        findedUserRoleList.get().forEach(userRole -> {
            Optional<Role> role = roleGateway.findById(userRole.getRole().getId());
            role.ifPresent(r -> userRoleList.add(new RoleDto(r.getName(), r.getAuthority())));
        });


        return userRoleList;

    }
}

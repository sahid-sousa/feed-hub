package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.RoleGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.usecases.security.user.ListUser;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.RoleDto;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.utils.GenericBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListUserImpl implements ListUser {

    private final UserGateway userGateway;
    private final UserRoleGateway userRoleGateway;

    public ListUserImpl(
            UserGateway userGateway,
            RoleGateway roleGateway,
            UserRoleGateway userRoleGateway
    ) {
        this.userGateway = userGateway;
        this.userRoleGateway = userRoleGateway;
    }

    @Override
    public PageListResponse<UserResponse> list(String name, String username, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        String nameFilter = StringUtils.hasText(name) ? name : "";
        String usernameFilter = StringUtils.hasText(username) ? username : "";

        Page<User> usersPage = userGateway.findAllByFilters(nameFilter, usernameFilter, pageable);

        Page<UserResponse> responsePage = usersPage.map(user ->
                GenericBuilder.of(UserResponse::new)
                        .with(UserResponse::setId, user.getId())
                        .with(UserResponse::setName, user.getName())
                        .with(UserResponse::setUsername, user.getUsername())
                        .with(UserResponse::setEmail, user.getEmail())
                        .with(UserResponse::setPassword, "")
                        .with(UserResponse::setAccountNonExpired, user.isAccountNonExpired())
                        .with(UserResponse::setAccountNonLocked, user.isAccountNonLocked())
                        .with(UserResponse::setCredentialsNonExpired, user.isCredentialsNonExpired())
                        .with(UserResponse::setEnabled, user.isEnabled())
                        .with(UserResponse::setAuthorities, getRoles(user))
                        .build()
        );

        return PageListResponse.of(responsePage);
    }

    public List<RoleDto> getRoles(User user) {
        List<RoleDto > roles = new ArrayList<>();
        userRoleGateway.findAllByUser(user).ifPresent(userRoles -> {
            userRoles.forEach(userRole -> {
                roles.add(
                        GenericBuilder.of(RoleDto::new)
                                .with(RoleDto::setAuthority, userRole.getRole().getAuthority())
                                .with(RoleDto::setDescription, userRole.getRole().getName())
                                .build()
                );
            });
        });
        return roles;
    }
}

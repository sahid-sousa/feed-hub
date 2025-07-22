package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.usecases.security.user.CreateRole;
import br.com.feedhub.application.usecases.security.user.CreateUserRole;
import br.com.feedhub.application.usecases.security.user.UpdateUser;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.RoleDto;
import br.com.feedhub.interfaces.dto.request.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.interfaces.exceptions.ResourceFoundException;
import br.com.feedhub.utils.GenericBuilder;
import br.com.feedhub.utils.Validations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UpdateUserImpl implements UpdateUser {

    private final PasswordEncoder passwordEncoder;
    private final UserGateway userGateway;
    private final CreateRole createRole;
    private final CreateUserRole createUserRole;
    private final UserRoleGateway userRoleGateway;
    private final Validations validations;

    public UpdateUserImpl(
            PasswordEncoder passwordEncoder,
            UserGateway userGateway,
            CreateRole createRole,
            CreateUserRole createUserRole,
            UserRoleGateway userRoleGateway,
            Validations validations
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userGateway = userGateway;
        this.createRole = createRole;
        this.createUserRole = createUserRole;
        this.userRoleGateway = userRoleGateway;
        this.validations = validations;
    }

    @Override
    public UserResponse update(UserUpdateRequest userUpdateRequest) {

        validations.isNameUsernamePasswordEmailOrAuthoritiesEmpty(userUpdateRequest);
        validations.isValidEmail(userUpdateRequest);

        if (userGateway.findByUsername(userUpdateRequest.getUsername()).isPresent()) {
            throw new ResourceFoundException("Username already exists");
        }
        if (userGateway.findByEmail(userUpdateRequest.getEmail()).isPresent()) {
            throw new ResourceFoundException("Email already exists");
        }

        User userUpdated = updateUser(userUpdateRequest);
        updateRoles(userUpdated, userUpdateRequest);

        return  GenericBuilder.of(UserResponse::new)
                .with(UserResponse::setId, userUpdated.getId())
                .with(UserResponse::setName,userUpdated.getName())
                .with(UserResponse::setUsername, userUpdated.getUsername())
                .with(UserResponse::setEmail, userUpdated.getEmail())
                .with(UserResponse::setPassword, "")
                .with(UserResponse::setAccountNonExpired, userUpdated.isAccountNonExpired())
                .with(UserResponse::setAccountNonLocked, userUpdated.isAccountNonLocked())
                .with(UserResponse::setCredentialsNonExpired, userUpdated.isCredentialsNonExpired())
                .with(UserResponse::setEnabled, userUpdated.isEnabled())
                .with(UserResponse::setAuthorities, getRoles(userUpdateRequest))
                .build();
    }

    public List<RoleDto> getRoles(UserUpdateRequest userCreateRequest) {
        return userCreateRequest.getAuthorities().stream().map(authority ->
                GenericBuilder.of(RoleDto::new)
                        .with(RoleDto::setAuthority, authority.getAuthority())
                        .with(RoleDto::setDescription, authority.getAuthority().replace("ROLE_", ""))
                        .build()).toList();
    }

    public User updateUser(UserUpdateRequest userUpdateRequest) {
        Optional<User> userFound = userGateway.findById(userUpdateRequest.getId());
        if (userFound.isEmpty()) {
            throw new RequiredObjectIsNullException("User with id: " + userUpdateRequest.getId() + " not found");
        }
        User userUpdated = GenericBuilder.of(userFound.get())
                .with(User::setName, userUpdateRequest.getName())
                .with(User::setUsername, userUpdateRequest.getUsername())
                .with(User::setEmail, userUpdateRequest.getEmail())
                .with(User::setPassword, passwordEncoder.encode(userUpdateRequest.getPassword()))
                .with(User::setAccountNonExpired, userUpdateRequest.isAccountNonExpired())
                .with(User::setAccountNonLocked, userUpdateRequest.isAccountNonLocked())
                .with(User::setCredentialsNonExpired, userUpdateRequest.isCredentialsNonExpired())
                .with(User::setEnabled, userUpdateRequest.isEnabled())
                .build();
        return userGateway.save(userUpdated);
    }

    public void updateRoles(User userUpdated, UserUpdateRequest userUpdateRequest) {
        userRoleGateway.deleteAllByUser(userUpdated);
        List<Role> roles = createRole.create(userUpdateRequest);
        createUserRole.create(userUpdated, roles);
    }
}

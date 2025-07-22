package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.security.user.CreateRole;
import br.com.feedhub.application.usecases.security.user.CreateUser;
import br.com.feedhub.application.usecases.security.user.CreateUserRole;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.RoleDto;
import br.com.feedhub.interfaces.dto.request.UserCreateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.interfaces.exceptions.ResourceFoundException;
import br.com.feedhub.utils.GenericBuilder;
import br.com.feedhub.utils.Validations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateUserImpl implements CreateUser {

    private final PasswordEncoder passwordEncoder;
    private final UserGateway userGateway;
    private final CreateRole createRole;
    private final CreateUserRole createUserRole;
    private final Validations validations;

    public CreateUserImpl(
            PasswordEncoder passwordEncoder,
            UserGateway userGateway,
            CreateRole createRole,
            CreateUserRole createUserRole,
            Validations validations
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userGateway = userGateway;
        this.createRole = createRole;
        this.createUserRole = createUserRole;
        this.validations = validations;
    }

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {
        validations.isNameUsernamePasswordEmailOrAuthoritiesEmpty(userCreateRequest);
        validations.isValidEmail(userCreateRequest);

        if (userGateway.findByUsername(userCreateRequest.getUsername()).isPresent()) {
            throw new ResourceFoundException("Username already exists");
        }
        if (userGateway.findByEmail(userCreateRequest.getEmail()).isPresent()) {
            throw new ResourceFoundException("Email already exists");
        }

        User userCreated = createUser(userCreateRequest);
        List<Role> roles = createRole.create(userCreateRequest);
        createUserRole.create(userCreated, roles);
        return GenericBuilder.of(UserResponse::new)
                .with(UserResponse::setId, userCreated.getId())
                .with(UserResponse::setName,userCreated.getName())
                .with(UserResponse::setUsername, userCreated.getUsername())
                .with(UserResponse::setEmail, userCreated.getEmail())
                .with(UserResponse::setPassword, "")
                .with(UserResponse::setAccountNonExpired, userCreated.isAccountNonExpired())
                .with(UserResponse::setAccountNonLocked, userCreated.isAccountNonLocked())
                .with(UserResponse::setCredentialsNonExpired, userCreated.isCredentialsNonExpired())
                .with(UserResponse::setEnabled, userCreated.isEnabled())
                .with(UserResponse::setAuthorities, getRoles(userCreateRequest))
                .build();
    }

    public List<RoleDto> getRoles(UserCreateRequest userCreateRequest) {
        return userCreateRequest.getAuthorities().stream().map(authority ->
                GenericBuilder.of(RoleDto::new)
                        .with(RoleDto::setAuthority, authority.getAuthority())
                        .with(RoleDto::setDescription, authority.getAuthority().replace("ROLE_", ""))
                        .build()).toList();
    }

    public User createUser(UserCreateRequest userCreateRequest) {
        User user = GenericBuilder.of(User::new)
                .with(User::setName, userCreateRequest.getName())
                .with(User::setUsername, userCreateRequest.getUsername())
                .with(User::setEmail, userCreateRequest.getEmail())
                .with(User::setPassword, passwordEncoder.encode(userCreateRequest.getPassword()))
                .with(User::setAccountNonExpired, userCreateRequest.isAccountNonExpired())
                .with(User::setAccountNonLocked, userCreateRequest.isAccountNonLocked())
                .with(User::setCredentialsNonExpired, userCreateRequest.isCredentialsNonExpired())
                .with(User::setEnabled, userCreateRequest.isEnabled())
                .build();
        return userGateway.save(user);
    }

}

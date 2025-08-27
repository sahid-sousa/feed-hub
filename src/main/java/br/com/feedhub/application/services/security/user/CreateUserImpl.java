package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.security.user.CreateRole;
import br.com.feedhub.application.usecases.security.user.CreateUser;
import br.com.feedhub.application.usecases.security.user.CreateUserRole;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.request.user.UserCreateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.interfaces.exceptions.PropertiesNotValidException;
import br.com.feedhub.interfaces.exceptions.ResourceFoundException;
import br.com.feedhub.utils.GenericBuilder;
import br.com.feedhub.utils.Validations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CreateUserImpl implements CreateUser {

    private final PasswordEncoder passwordEncoder;
    private final UserGateway userGateway;
    private final CreateRole createRole;
    private final CreateUserRole createUserRole;
    private final Validations validations;

    private final String[] ROLES = {"ROLE_USER"};

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
    public UserResponse execute(UserCreateRequest userCreateRequest) {
        if (!validations.isValidEmail(userCreateRequest.getEmail())) {
            throw new PropertiesNotValidException("Attribute email: " + userCreateRequest.getEmail()  + " not valid");
        }
        if (userGateway.findByUsername(userCreateRequest.getUsername()).isPresent()) {
            throw new ResourceFoundException("Username already exists");
        }
        if (userGateway.findByEmail(userCreateRequest.getEmail()).isPresent()) {
            throw new ResourceFoundException("Email already exists");
        }
        User userCreated = createUser(userCreateRequest);
        List<Role> roles = createRole.execute(ROLES);
        createUserRole.execute(userCreated, roles);
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
                .with(UserResponse::setAuthorities, getRoles(ROLES))
                .build();
    }

    public List<RoleDto> getRoles(String[] roles) {
        return Arrays.stream(roles).map(authority ->
                GenericBuilder.of(RoleDto::new)
                        .with(RoleDto::setAuthority, authority)
                        .with(RoleDto::setDescription, authority.replace("ROLE_", ""))
                        .build()).toList();
    }

    public User createUser(UserCreateRequest userCreateRequest) {
        User user = GenericBuilder.of(User::new)
                .with(User::setName, userCreateRequest.getName())
                .with(User::setUsername, userCreateRequest.getUsername())
                .with(User::setEmail, userCreateRequest.getEmail())
                .with(User::setPassword, passwordEncoder.encode(userCreateRequest.getPassword()))
                .with(User::setAccountNonExpired, true)
                .with(User::setAccountNonLocked, true)
                .with(User::setCredentialsNonExpired, true)
                .with(User::setEnabled, true)
                .build();
        return userGateway.save(user);
    }

}

package br.com.feedhub.application.services.security.user;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.application.usecases.security.user.UpdateUser;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.request.user.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.interfaces.exceptions.PropertiesNotValidException;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.interfaces.exceptions.ResourceFoundException;
import br.com.feedhub.interfaces.exceptions.ResourceNotFoundException;
import br.com.feedhub.utils.GenericBuilder;
import br.com.feedhub.utils.Validations;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateUserImpl implements UpdateUser {

    private final ExtractUsername extractUsername;
    private final PasswordEncoder passwordEncoder;
    private final UserGateway userGateway;
    private final UserRoleGateway userRoleGateway;
    private final Validations validations;

    public UpdateUserImpl(
            ExtractUsername extractUsername,
            PasswordEncoder passwordEncoder,
            UserGateway userGateway,
            UserRoleGateway userRoleGateway,
            Validations validations
    ) {
        this.extractUsername = extractUsername;
        this.passwordEncoder = passwordEncoder;
        this.userGateway = userGateway;
        this.userRoleGateway = userRoleGateway;
        this.validations = validations;
    }

    @Override
    public UserResponse execute(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        String username = extractUsername.execute(request.getHeader("Authorization"));
        if (!validations.isValidEmail(userUpdateRequest.getEmail())) {
            throw new PropertiesNotValidException("Attribute email: " + userUpdateRequest.getEmail()  + " not valid");
        }
        if (userGateway.findByUsername(username).isEmpty()) {
            throw new ResourceNotFoundException("Username not exists");
        }
        if (userGateway.findByEmail(userUpdateRequest.getEmail()).isPresent()) {
            throw new ResourceFoundException("Email already exists");
        }
        User userUpdated = updateUser(userUpdateRequest, username);
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
                .with(UserResponse::setAuthorities, getRoles(userUpdated))
                .build();
    }

    public List<RoleDto> getRoles(User user) {
        return userRoleGateway.findAllByUser(user)
                .map(userRoles -> userRoles.stream()
                        .map(userRole -> GenericBuilder.of(RoleDto::new)
                                .with(RoleDto::setAuthority, userRole.getRole().getAuthority())
                                .with(RoleDto::setDescription, userRole.getRole().getAuthority().replace("ROLE_", ""))
                                .build())
                        .toList())
                .orElse(Collections.emptyList());
    }

    public User updateUser(UserUpdateRequest userUpdateRequest, String username) {
        Optional<User> userFound = userGateway.findByUsername(username);
        if (userFound.isEmpty()) {
            throw new RequiredObjectIsNullException("User with username: " + username + " not found");
        }
        User userUpdated = GenericBuilder.of(userFound.get())
                .with(User::setName, userUpdateRequest.getName())
                .with(User::setEmail, userUpdateRequest.getEmail())
                .with(User::setPassword, passwordEncoder.encode(userUpdateRequest.getPassword()))
                .build();
        return userGateway.save(userUpdated);
    }

}

package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.usecases.security.auth.Authenticate;
import br.com.feedhub.application.usecases.security.auth.GenerateToken;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.interfaces.dto.request.security.AccountCredentials;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import br.com.feedhub.interfaces.exceptions.InvalidAuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticateImpl implements Authenticate {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserGateway userGateway;
    private final UserRoleGateway userRoleGateway;
    private final GenerateToken generateToken;

    public AuthenticateImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserGateway userGateway, UserRoleGateway userRoleGateway, GenerateToken generateToken) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userGateway = userGateway;
        this.userRoleGateway = userRoleGateway;
        this.generateToken = generateToken;
    }

    @Override
    public TokenResponse signin(AccountCredentials credentials) {
        if (credentials.hasNullOrEmptyFields()) {
            throw new InvalidAuthenticationException("Invalid credentials");
        }
        var username = credentials.username();
        var password = credentials.password();
        Optional<User> user = userGateway.findByUsername(username);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new InvalidAuthenticationException("Invalid username or password supplied!");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        List<String> roles = new ArrayList<>();
        userRoleGateway.findAllByUser(user.get()).ifPresent(userRoles -> {
            for (UserRole role : userRoles) {
                roles.add(role.getRole().getAuthority());
            }
        });
        return generateToken.execute(username, roles);
    }

}

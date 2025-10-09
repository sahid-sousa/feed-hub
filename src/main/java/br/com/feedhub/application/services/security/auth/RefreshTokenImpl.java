package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.usecases.security.auth.GenerateToken;
import br.com.feedhub.application.usecases.security.auth.RefreshToken;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import br.com.feedhub.interfaces.exceptions.InvalidAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RefreshTokenImpl implements RefreshToken {

    private final GenerateToken generateToken;
    private final UserGateway userGateway;
    private final UserRoleGateway userRoleGateway;

    public RefreshTokenImpl(GenerateToken generateToken, UserGateway userGateway, UserRoleGateway userRoleGateway) {
        this.generateToken = generateToken;
        this.userGateway = userGateway;
        this.userRoleGateway = userRoleGateway;
    }

    @Override
    public TokenResponse execute(String username, String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidAuthenticationException("Invalid refresh token credentials");
        }
        Optional<User> user = userGateway.findByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidAuthenticationException("Invalid username supplied!");
        }
        List<String> roles = new ArrayList<>();
        userRoleGateway.findAllByUser(user.get()).ifPresent(userRoles -> {
            for (UserRole role : userRoles) {
                roles.add(role.getRole().getAuthority());
            }
        });
        return generateToken.execute(username, roles);
    }
}

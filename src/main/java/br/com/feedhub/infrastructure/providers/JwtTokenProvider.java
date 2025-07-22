package br.com.feedhub.infrastructure.providers;

import br.com.feedhub.application.usecases.security.auth.AuthenticateToken;
import br.com.feedhub.application.usecases.security.auth.ResolveToken;
import br.com.feedhub.application.usecases.security.auth.ValidateToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    AuthenticateToken authenticateToken;
    ResolveToken resolveToken;
    ValidateToken validateToken;

    public JwtTokenProvider(AuthenticateToken authenticateToken, ResolveToken resolveToken, ValidateToken validateToken) {
        this.authenticateToken = authenticateToken;
        this.resolveToken = resolveToken;
        this.validateToken = validateToken;
    }

    public Authentication authenticate(String token) {
        return authenticateToken.authenticate(token);
    }

    public String resolve(String token) {
        return resolveToken.resolve(token);
    }

}

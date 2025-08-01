package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.application.usecases.security.auth.ResolveToken;
import br.com.feedhub.application.usecases.security.auth.TokenDecoder;
import br.com.feedhub.application.usecases.security.auth.ValidateToken;
import br.com.feedhub.interfaces.exceptions.InvalidJwtAuthenticationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

@Service
public class ExtractUsernameImpl implements ExtractUsername {

    private final ResolveToken resolveToken;
    private final TokenDecoder tokenDecoder;
    private final ValidateToken validateToken;

    public ExtractUsernameImpl(ResolveToken resolveToken, TokenDecoder tokenDecoder, ValidateToken validateToken) {
        this.resolveToken = resolveToken;
        this.tokenDecoder = tokenDecoder;
        this.validateToken = validateToken;
    }

    @Override
    public String execute(String jwtToken) {
        String token = resolveToken.execute(jwtToken);
        if (!validateToken.execute(token)) {
            throw new InvalidJwtAuthenticationException("Invalid token");
        }
        DecodedJWT decoded = tokenDecoder.decode(token);
        return decoded.getSubject();
    }
}

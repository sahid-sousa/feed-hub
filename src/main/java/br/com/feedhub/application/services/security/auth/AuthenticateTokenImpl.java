package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.application.usecases.security.auth.AuthenticateToken;
import br.com.feedhub.application.usecases.security.auth.LoadUserDetails;
import br.com.feedhub.application.usecases.security.auth.TokenDecoder;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateTokenImpl implements AuthenticateToken {

    private final TokenDecoder tokenDecoder;
    private final LoadUserDetails userDetails;

    public AuthenticateTokenImpl(TokenDecoder tokenDecoder, LoadUserDetails userDetails) {
        this.tokenDecoder = tokenDecoder;
        this.userDetails = userDetails;
    }

    @Override
    public Authentication execute(String token) {
        DecodedJWT decoded = tokenDecoder.decode(token);
        UserDetails details = userDetails.loadByUsername(decoded.getSubject());
        return new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
    }
}

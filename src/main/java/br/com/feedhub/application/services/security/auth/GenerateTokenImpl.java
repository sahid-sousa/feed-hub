package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.application.usecases.security.auth.GenerateToken;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class GenerateTokenImpl implements GenerateToken {

    private final String SECRET_KEY;
    private final long VALIDITY;
    private final String ISSUER;

    public GenerateTokenImpl(
            @Value("${jwt.secret.key}") String SECRET_KEY,
            @Value("${jwt.secret.validity}") long VALIDITY,
            @Value("${jwt.secret.issuer}") String ISSUER
    ) {
        this.SECRET_KEY = SECRET_KEY;
        this.ISSUER = ISSUER;
        this.VALIDITY = VALIDITY;
    }

    @Override
    public TokenResponse execute(String username, List<String> roles) {
        final ZoneId ZONE_ID = ZoneId.systemDefault();
        final LocalDateTime NOW = LocalDateTime.now();
        final LocalDateTime EXPIRES_AT = NOW.plus(Duration.ofMillis(VALIDITY));
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String token = JWT.create()
                .withClaim("roles", roles)
                .withIssuer(ISSUER)
                .withIssuedAt(Date.from(NOW.atZone(ZONE_ID).toInstant()))
                .withExpiresAt(Date.from(EXPIRES_AT.atZone(ZONE_ID).toInstant()))
                .withSubject(username)
                .sign(algorithm)
                .strip();
        String refreshToken = JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(Date.from(NOW.atZone(ZONE_ID).toInstant()))
                .withExpiresAt(Date.from(EXPIRES_AT.atZone(ZONE_ID).toInstant()))
                .withSubject(username)
                .sign(algorithm)
                .strip();
        return new TokenResponse(username, token, refreshToken, NOW, EXPIRES_AT);
    }

}

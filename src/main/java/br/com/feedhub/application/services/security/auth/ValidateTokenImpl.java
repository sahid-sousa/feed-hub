package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.application.usecases.security.auth.ValidateToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ValidateTokenImpl implements ValidateToken {

    private final String SECRET_KEY;

    public ValidateTokenImpl(
            @Value("${jwt.secret.key}") String SECRET_KEY
    ) {
        this.SECRET_KEY = SECRET_KEY;
    }

    @Override
    public Boolean execute(String token) {
        Algorithm alg = Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decoded = verifier.verify(token);
        Date expiresAt = decoded.getExpiresAt();
        LocalDateTime expiration = expiresAt
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return !expiration.isBefore(LocalDateTime.now());
    }
}

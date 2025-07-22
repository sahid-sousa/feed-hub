package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.application.usecases.security.auth.TokenDecoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenDecoderImpl implements TokenDecoder {

    private final String SECRET_KEY;

    public TokenDecoderImpl(
            @Value("${jwt.secret.key}") String SECRET_KEY
    ) {
        this.SECRET_KEY = SECRET_KEY;
    }

    @Override
    public DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
    }
}

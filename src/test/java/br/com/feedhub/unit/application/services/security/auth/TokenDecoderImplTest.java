package br.com.feedhub.unit.application.services.security.auth;

import br.com.feedhub.application.services.security.auth.TokenDecoderImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenDecoderImplTest {

    @InjectMocks
    TokenDecoderImpl tokenDecoder;

    private static final String SECRET_KEY = "4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P";
    private String token;

    @BeforeEach
    public void setup() {
        //Given
        tokenDecoder = new TokenDecoderImpl(SECRET_KEY);
        token = JWT.create()
                .withSubject("user-test")
                .withClaim("role", "ROLE_USER")
                .sign(Algorithm.HMAC256(SECRET_KEY));

    }

    @Test
    @DisplayName("Test should return DecodedJWT when token is provided")
    void testShouldReturnDecodedJWT_whenTokenIsProvided() {
        //When
        DecodedJWT decoded = tokenDecoder.decode(token);

        //Then
        assertEquals("user-test", decoded.getSubject());
        assertEquals("ROLE_USER", decoded.getClaim("role").asString());
        assertFalse(decoded.getToken().isEmpty());
    }

}

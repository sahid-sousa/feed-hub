package br.com.feedhub.unit.application.usecases.security.auth;

import br.com.feedhub.application.usecases.security.auth.TokenDecoder;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenDecoderTest {

    @Mock
    TokenDecoder tokenDecoder;

    @Mock
    DecodedJWT decoded;

    @Test
    @DisplayName("Test should return DecodedJWT when token is provided")
    void testShouldReturnDecodedJWT_whenTokenIsProvided() {
        //Given
        given(decoded.getSubject()).willReturn("user-test");
        given(decoded.getToken()).willReturn("token-test");
        given(tokenDecoder.decode(anyString())).willReturn(decoded);

        //When
        DecodedJWT decodedJWT = tokenDecoder.decode(anyString());

        //Then
        assertEquals("user-test", decodedJWT.getSubject());
        assertFalse(decodedJWT.getToken().isEmpty());
    }

}

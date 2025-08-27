package br.com.feedhub.unit.application.services.security.auth;

import br.com.feedhub.application.services.security.auth.ExtractUsernameImpl;
import br.com.feedhub.application.usecases.security.auth.ResolveToken;
import br.com.feedhub.application.usecases.security.auth.TokenDecoder;
import br.com.feedhub.application.usecases.security.auth.ValidateToken;
import br.com.feedhub.interfaces.exceptions.InvalidJwtAuthenticationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ExtractUsernameImplTest {

    @InjectMocks
    ExtractUsernameImpl extractUsernameImpl;

    @Mock
    ResolveToken resolveToken;

    @Mock
    TokenDecoder tokenDecoder;

    @Mock
    ValidateToken validateToken;

    @Mock
    DecodedJWT decoded;

    @Test
    @DisplayName("Test should return username when token is valid")
    void testShouldReturnUsername_whenTokenIsValid() {
        //Given
        given(decoded.getSubject()).willReturn("user-test");
        given(resolveToken.execute(anyString())).willReturn("token-test");
        given(validateToken.execute(anyString())).willReturn(true);
        given(tokenDecoder.decode(anyString())).willReturn(decoded);

        //When
        String username = extractUsernameImpl.execute(anyString());

        //Then
        assertEquals("user-test", username);
    }

    @Test
    @DisplayName("Test should return Exception when token is invalid")
    void testShouldReturnException_whenTokenIsInvalid() {
        //Given
        given(resolveToken.execute(anyString())).willReturn("token-test");
        given(validateToken.execute(anyString())).willReturn(false);

        //When
        InvalidJwtAuthenticationException exception = assertThrows(
                InvalidJwtAuthenticationException.class,
                () -> extractUsernameImpl.execute(anyString())
        );

        //Then
        assertEquals("Invalid token", exception.getMessage());
    }

}

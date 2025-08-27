package br.com.feedhub.unit.application.usecases.security.auth;

import br.com.feedhub.application.usecases.security.auth.AuthenticateToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthenticateTokenTest {

    @Mock
    AuthenticateToken authenticateToken;

    @Mock
    Authentication authentication;

    @Test
    @DisplayName("Test should return Authentication when Token is Informed")
    void testShouldReturnAuthentication_whenTokenInformed() {
        //Given
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn("principal");
        given(authentication.getCredentials()).willReturn("password");
        given(authenticateToken.execute(anyString())).willReturn(authentication);

        //When
        Authentication authenticated = authenticateToken.execute(anyString());

        //Then
        assertNotNull(authenticated);
        assertTrue(authenticated.isAuthenticated());
        assertNotNull(authenticated.getPrincipal());
        assertNotNull(authenticated.getCredentials());
    }
}

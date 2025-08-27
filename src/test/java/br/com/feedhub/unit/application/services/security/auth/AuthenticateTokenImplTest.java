package br.com.feedhub.unit.application.services.security.auth;

import br.com.feedhub.application.services.security.auth.AuthenticateTokenImpl;
import br.com.feedhub.application.usecases.security.auth.LoadUserDetails;
import br.com.feedhub.application.usecases.security.auth.TokenDecoder;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthenticateTokenImplTest {

    @InjectMocks
    AuthenticateTokenImpl authenticateToken;

    @Mock
    TokenDecoder tokenDecoder;

    @Mock
    DecodedJWT decoded;

    @Mock
    LoadUserDetails userDetails;

    private UserResponse userResponse;

    @BeforeEach
    public void setup() {
        RoleDto roleDto = new RoleDto("USER", "USER_ROLE");
        userResponse = new UserResponse(
                1L,
                "user",
                "user-test",
                "",
                true,
                true,
                true,
                true,
                "email@email.com",
                List.of(roleDto)
        );
    }

    @Test
    @DisplayName("Test should return Authentication when Token is Informed")
    void testShouldReturnAuthentication_whenTokenInformed() {
        //Given
        given(decoded.getSubject()).willReturn("user-test");
        given(tokenDecoder.decode(anyString())).willReturn(decoded);
        given(userDetails.loadByUsername(anyString())).willReturn(userResponse);

        //When
        Authentication authentication = authenticateToken.execute(anyString());

        //Then
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
        assertNotNull(authentication.getPrincipal());
        assertNotNull(authentication.getCredentials());
    }



}

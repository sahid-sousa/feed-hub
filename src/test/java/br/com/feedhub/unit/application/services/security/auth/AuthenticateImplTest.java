package br.com.feedhub.unit.application.services.security.auth;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.services.security.auth.AuthenticateImpl;
import br.com.feedhub.application.usecases.security.auth.GenerateToken;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.interfaces.dto.request.security.AccountCredentials;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import br.com.feedhub.interfaces.exceptions.InvalidAuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthenticateImplTest {

    @InjectMocks
    AuthenticateImpl authenticate;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserGateway userGateway;

    @Mock
    UserRoleGateway userRoleGateway;

    @Mock
    GenerateToken generateToken;

    private AccountCredentials accountCredentials;
    private User user;
    private UserRole userRole;
    private TokenResponse tokenResponse;

    @BeforeEach
    public void setup() {
        //Given
        accountCredentials = new AccountCredentials("user-test", "123456");
        user = new User();
        user.setId(1L);
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        Role role = new Role();
        role.setId(1L);
        role.setAuthority("ROLE_USER");
        role.setName("USER");

        userRole = new UserRole();
        userRole.setId(1L);
        userRole.setUser(user);
        userRole.setRole(role);

        tokenResponse = new TokenResponse(
                "user-test",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Test should return Jwt token when login is successful")
    void testShouldReturnJwtToken_whenLoginIsSuccessful() {
        //Given
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(userRoleGateway.findAllByUser(any())).willReturn(Optional.of(List.of(userRole)));
        given(generateToken.execute(anyString(), anyList())).willReturn(tokenResponse);
        given(authenticationManager.authenticate(any())).willReturn(authentication);
        //When
        TokenResponse response = authenticate.signin(accountCredentials);

        //Then
        assertNotNull(response);
        assertEquals("user-test", tokenResponse.username());
        assertEquals(tokenResponse, response);
    }

    @Test
    @DisplayName("Test should return Exception when credentials is invalid")
    void testShouldReturnException_whenCredentialsIsInvalid() {
        //When
        InvalidAuthenticationException exception = assertThrows(
                InvalidAuthenticationException.class,
                () -> authenticate.signin(new AccountCredentials("", ""))
        );

        //Then
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    @DisplayName("Test should return Exception when password not match")
    void testShouldReturnException_whenPasswordNotMatch() {
        //Given
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        //When
        InvalidAuthenticationException exception = assertThrows(
                InvalidAuthenticationException.class,
                () -> authenticate.signin(accountCredentials)
        );

        //Then
        assertEquals("Invalid username or password supplied!", exception.getMessage());

    }

    @Test
    @DisplayName("Test should return Exception when User not found")
    void testShouldReturnException_whenUserNotFound() {
        //Given
        given(userGateway.findByUsername(anyString())).willReturn(Optional.empty());

        //When

        InvalidAuthenticationException exception = assertThrows(
                InvalidAuthenticationException.class,
                () -> authenticate.signin(accountCredentials)
        );

        //Then
        assertEquals("Invalid username or password supplied!", exception.getMessage());

    }
}

package br.com.feedhub.unit.application.services.security.user;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.services.security.user.UpdateUserImpl;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.user.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.interfaces.exceptions.PropertiesNotValidException;
import br.com.feedhub.interfaces.exceptions.ResourceFoundException;
import br.com.feedhub.interfaces.exceptions.ResourceNotFoundException;
import br.com.feedhub.utils.Validations;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateUserImplTest {

    @InjectMocks
    UpdateUserImpl updateUser;

    @Mock
    ExtractUsername extractUsername;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserGateway userGateway;

    @Mock
    UserRoleGateway userRoleGateway;

    @Mock
    HttpServletRequest request;

    @Mock
    Validations validations;

    private UserUpdateRequest userUpdateRequest;
    private User user;
    private String bearerToken;

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setId(1L);
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        userUpdateRequest = new UserUpdateRequest(
                "User-updated",
                "123456",
                "email@email.com.br"
        );

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
        bearerToken =  "Bearer " + token;

    }

    @Test
    @DisplayName("Test Receive UserUpdateRequest when update then Return UserResponse")
    void testReceiveUserUpdateRequest_whenUpdate_thenReturnUserResponse() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(validations.isValidEmail(anyString())).willReturn(true);
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(userGateway.findByEmail(anyString())).willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("123456");
        given(userGateway.save(any())).willReturn(user);

        //When
        UserResponse response = updateUser.execute(userUpdateRequest, request);

        //Then
        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getUsername(), response.getUsername());
    }

    @Test
    @DisplayName("Test Receive UserUpdateRequest when update then Return Exception when email not valid")
    void testReceiveUserUpdateRequest_whenUpdate_thenException_whenEmailNotValid() {
        //Given
        given(validations.isValidEmail(anyString())).willReturn(false);

        //When
        PropertiesNotValidException exception = assertThrows(
          PropertiesNotValidException.class,
                () -> updateUser.execute(userUpdateRequest, request)
        );

        //Then
        assertEquals("Attribute email: " + userUpdateRequest.getEmail()  + " not valid", exception.getMessage());
    }

    @Test
    @DisplayName("Test Receive UserUpdateRequest when update then Return Exception when username not exists")
    void testReceiveUserUpdateRequest_whenUpdate_thenException_whenUsernameNotExists() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(validations.isValidEmail(anyString())).willReturn(true);
        given(userGateway.findByUsername(anyString())).willReturn(Optional.empty());

        //When
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> updateUser.execute(userUpdateRequest, request)
        );

        //Then
        assertEquals("Username not exists", exception.getMessage());
    }

    @Test
    @DisplayName("Test Receive UserUpdateRequest when update then Return Exception when email exists")
    void testReceiveUserUpdateRequest_whenUpdate_thenException_whenEmailExists() {
        //Given
        given(request.getHeader("Authorization")).willReturn(bearerToken);
        given(extractUsername.execute(anyString())).willReturn("user-test");
        given(validations.isValidEmail(anyString())).willReturn(true);
        given(userGateway.findByUsername(anyString())).willReturn(Optional.of(user));
        given(userGateway.findByEmail(anyString())).willReturn(Optional.of(user));

        //When
        ResourceFoundException exception = assertThrows(
                ResourceFoundException.class,
                () -> updateUser.execute(userUpdateRequest, request)
        );

        //Then
        assertEquals("Email already exists", exception.getMessage());
    }

}

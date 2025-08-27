package br.com.feedhub.unit.application.services.security.user;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.services.security.user.CreateUserImpl;
import br.com.feedhub.application.usecases.security.user.CreateRole;
import br.com.feedhub.application.usecases.security.user.CreateUserRole;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.request.user.UserCreateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.interfaces.exceptions.PropertiesNotValidException;
import br.com.feedhub.interfaces.exceptions.ResourceFoundException;
import br.com.feedhub.utils.Validations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class CreateUserImplTest {

    @InjectMocks
    CreateUserImpl createUserImpl;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserGateway userGateway;

    @Mock
    CreateRole createRole;

    @Mock
    CreateUserRole createUserRole;

    @Mock
    Validations validations;

    private User user;
    private Role role;
    private UserCreateRequest userCreateRequest;
    private final String[] ROLES = {"ROLE_USER"};

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        role = new Role();
        role.setId(1L);
        role.setAuthority("ROLE_USER");
        role.setName("USER");

        userCreateRequest = new UserCreateRequest(
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );
    }

    @Test
    @DisplayName("Test receive UserCreateRequest when create then return UserResponse")
    void testReceiveUserCreateRequest_whenCreate_thenReturnUserResponse() {
        //Given
        willDoNothing().given(createUserRole).execute(user, List.of(role));
        given(validations.isValidEmail(userCreateRequest.getEmail())).willReturn(true);
        given(userGateway.findByUsername(userCreateRequest.getUsername())).willReturn(Optional.empty());
        given(userGateway.findByEmail(userCreateRequest.getEmail())).willReturn(Optional.empty());
        given(createRole.execute(ROLES)).willReturn(List.of(role));
        given(passwordEncoder.encode(userCreateRequest.getPassword())).willReturn("123456");
        given(userGateway.save(any())).willReturn(user);

        //When
        UserResponse userResponse = createUserImpl.execute(userCreateRequest);

        //Then
        assertNotNull(userResponse);
        assertEquals(userCreateRequest.getUsername(), userResponse.getUsername());
        assertEquals(userCreateRequest.getEmail(), userResponse.getEmail());
        assertEquals(userCreateRequest.getEmail(), userResponse.getEmail());
    }

    @Test
    @DisplayName("Test receive UserCreateRequest when create then return Exception when email is not valid")
    void testReceiveUserCreateRequest_whenCreate_thenReturnExceptionWhenEmailIsNotValid() {
        //Given
        given(validations.isValidEmail(userCreateRequest.getEmail())).willReturn(false);

        //When
        PropertiesNotValidException exception = assertThrows(
                PropertiesNotValidException.class,
                () -> createUserImpl.execute(userCreateRequest)
        );

        //Then
        assertEquals("Attribute email: "+ userCreateRequest.getEmail() +" not valid", exception.getMessage());
    }

    @Test
    @DisplayName("Test receive UserCreateRequest when create then return Exception when username exists")
    void testReceiveUserCreateRequest_whenCreate_thenReturnExceptionWhenUsernameExists() {
        //Given
        given(validations.isValidEmail(userCreateRequest.getEmail())).willReturn(true);
        given(userGateway.findByUsername(userCreateRequest.getUsername())).willReturn(Optional.of(user));
        //When
        ResourceFoundException exception = assertThrows(
                ResourceFoundException.class,
                () -> createUserImpl.execute(userCreateRequest)
        );

        //Then
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Test receive UserCreateRequest when create then return Exception when Email exists")
    void testReceiveUserCreateRequest_whenCreate_thenReturnExceptionWhenEmailExists() {
        //Given
        given(validations.isValidEmail(userCreateRequest.getEmail())).willReturn(true);
        given(userGateway.findByUsername(userCreateRequest.getUsername())).willReturn(Optional.empty());
        given(userGateway.findByEmail(userCreateRequest.getEmail())).willReturn(Optional.of(user));

        //When
        ResourceFoundException exception = assertThrows(
                ResourceFoundException.class,
                () -> createUserImpl.execute(userCreateRequest)
        );

        //Then
        assertEquals("Email already exists", exception.getMessage());
    }



}

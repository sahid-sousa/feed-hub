package br.com.feedhub.unit.application.services.security.user;

import br.com.feedhub.adapters.database.user.RoleGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.services.security.user.FindUserImpl;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class FindUserImplTest {

    @InjectMocks
    private FindUserImpl findUserImpl;

    @Mock
    UserGateway userGateway;

    @Mock
    RoleGateway roleGateway;

    @Mock
    UserRoleGateway userRoleGateway;

    private User user;
    private Role role;
    private UserRole userRole;

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setId(1L);
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        role = new Role();
        role.setId(1L);
        role.setName("USER");
        role.setAuthority("ROLE_USER");

        userRole = new UserRole();
        userRole.setId(1L);
        userRole.setRole(role);
        userRole.setUser(user);
    }

    @Test
    @DisplayName("Test Given User Object when findByUsername User then Return Saved User")
    void testReceiveLongId_whenFindUser_thenReturnUserResponse() {
        //Given
        given(userGateway.findById(anyLong())).willReturn(Optional.of(user));
        given(userRoleGateway.findAllByUser(any())).willReturn(Optional.of(List.of(userRole)));
        given(roleGateway.findById(anyLong())).willReturn(Optional.of(role));

        //When
        UserResponse response = findUserImpl.execute(user.getId());

        //Then
        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    @DisplayName("test Receive Long Id when findUser then exception when User not found")
    void testReceiveLongId_whenFindUser_thenException_whenUserNotFound() {
        //Given
        given(userGateway.findById(anyLong())).willReturn(Optional.empty());

        //When
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> findUserImpl.execute(user.getId())
        );

        //Then
        assertEquals("User not found for id " + user.getId(), exception.getMessage());
    }


}

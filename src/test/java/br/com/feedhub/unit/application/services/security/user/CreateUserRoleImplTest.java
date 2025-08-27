package br.com.feedhub.unit.application.services.security.user;

import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.services.security.user.CreateUserRoleImpl;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserRoleImplTest {

    @InjectMocks
    CreateUserRoleImpl createUserRole;

    @Mock
    UserRoleGateway userRoleGateway;

    private User user;
    private Role role;
    private UserRole userRole;

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

        userRole = new UserRole();
        userRole.setId(1L);
        userRole.setRole(role);
        userRole.setUser(user);
    }

    @Test
    @DisplayName("Test create UserRole when provided User and list of Roles then do nothing")
    void testCreateUserRole_whenProvidedUserAndListRoles_thenDoNothing() {
        //Given
        given(userRoleGateway.save(any())).willReturn(userRole);

        //When
        createUserRole.execute(user, List.of(role));
        //Then
        verify(userRoleGateway, times(1)).save(any());
    }



}

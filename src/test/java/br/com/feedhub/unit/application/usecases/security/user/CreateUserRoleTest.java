package br.com.feedhub.unit.application.usecases.security.user;

import br.com.feedhub.application.usecases.security.user.CreateUserRole;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserRoleTest {

    @Mock
    CreateUserRole createUserRole;

    private Role role;
    private User user;

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

    }


    @Test
    @DisplayName("Test create UserRole when provided User and list of Roles then do nothing")
    void testCreateUserRole_whenProvidedUserAndListRoles_thenDoNothing() {
        //When
        createUserRole.execute(user, List.of(role));

        //Then
        verify(createUserRole, times(1)).execute(user, List.of(role));
    }



}

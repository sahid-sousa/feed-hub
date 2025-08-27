package br.com.feedhub.unit.application.services.security.user;

import br.com.feedhub.adapters.database.user.RoleGateway;
import br.com.feedhub.application.services.security.user.CreateRoleImpl;
import br.com.feedhub.domain.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreateRoleImplTest {

    @InjectMocks
    private CreateRoleImpl createRole;

    @Mock
    RoleGateway roleGateway;

    private Role role;

    String[] roles;

    @BeforeEach
    public void setup() {
        //Given
        role = new Role();
        role.setId(1L);
        role.setAuthority("ROLE_USER");
        role.setName("USER");

        roles = new String[]{ role.getAuthority() };
    }

    @Test
    @DisplayName("Test Given Role Object when Save Role then Return Saved Role")
    void testGivenRoleObject_whenSaveRole_theReturnSavedRole() {
        //Given
        given(roleGateway.save(any())).willReturn(role);

        //When
        List<Role> roleList = createRole.execute(roles);

        //Then
        assertNotNull(roleList);
        assertEquals(1, roleList.size());
        assertEquals(role.getId(), roleList.getFirst().getId());
        assertEquals(role.getAuthority(), roleList.getFirst().getAuthority());
        assertEquals(role.getName(), roleList.getFirst().getName());
    }
}

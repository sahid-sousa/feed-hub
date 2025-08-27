package br.com.feedhub.unit.application.usecases.security.user;

import br.com.feedhub.application.usecases.security.user.CreateRole;
import br.com.feedhub.domain.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreateRoleTest {

    @Mock
    private CreateRole createRole;

    private Role role;

    @BeforeEach
    public void setup() {
        //Given
        role = new Role();
        role.setId(1L);
        role.setAuthority("ROLE_USER");
        role.setName("USER");
    }


    @Test
    @DisplayName("Test Given Role Object when Save Role then Return Saved Role")
    void testGivenRoleObject_whenSaveRole_theReturnSavedRole() {
        //Given
        given(createRole.execute(any())).willReturn(List.of(role));

        //When
        List<Role> roleList = createRole.execute(any());

        //Then
        assertNotNull(roleList);
        assertEquals(1, roleList.size());
        assertEquals(role.getId(), roleList.getFirst().getId());
        assertEquals(role.getAuthority(), roleList.getFirst().getAuthority());
        assertEquals(role.getName(), roleList.getFirst().getName());
    }
}

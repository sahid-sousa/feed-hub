package br.com.feedhub.unit.infrastructure.repository.security;


import br.com.feedhub.domain.security.Role;
import br.com.feedhub.infrastructure.repository.security.RoleRepository;
import br.com.feedhub.integration.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    RoleRepository repository;

    private Role role;

    @BeforeEach
    public void setup() {
        //Given
        role = new Role();
        role.setAuthority("ROLE_USER");
        role.setName("USER");
    }

    @Test
    @DisplayName("Test Given Role Object when Save Role then Return Saved Role")
    void testGivenRoleObject_whenSaveRole_theReturnSavedRole() {
        //When
        Role savedRole = repository.save(role);

        //Then
        assertNotNull(savedRole);
        assertEquals("ROLE_USER", savedRole.getAuthority());
        assertEquals("USER", savedRole.getName());
    }

}
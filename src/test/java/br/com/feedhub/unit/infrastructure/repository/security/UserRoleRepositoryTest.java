package br.com.feedhub.unit.infrastructure.repository.security;

import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.infrastructure.repository.security.RoleRepository;
import br.com.feedhub.infrastructure.repository.security.UserRepository;
import br.com.feedhub.infrastructure.repository.security.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRoleRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


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
        role.setAuthority("ROLE_USER");
        role.setName("USER");

        userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
    }

    @Test
    @DisplayName("Test Given UserRole Object when Save UserRole then Return Saved UserRole")
    void testGivenUserRoleObject_whenSaveUserRole_theReturnSavedUserRole() {
        //When
        userRepository.save(user);
        roleRepository.save(role);
        UserRole savedUserRole = userRoleRepository.save(userRole);

        //Then
        assertNotNull(savedUserRole);
        assertEquals("User", savedUserRole.getUser().getName());
        assertEquals("user-test", savedUserRole.getUser().getUsername());
        assertEquals("123456", savedUserRole.getUser().getPassword());
        assertEquals("email@email.com", savedUserRole.getUser().getEmail());

        assertEquals("ROLE_USER", savedUserRole.getRole().getAuthority());
        assertEquals("USER", savedUserRole.getRole().getName());
    }

    @Test
    @DisplayName("Test Given UserRole Object when Save UserRole then Return Saved UserRole")
    void testGivenListUserRoleObject_whenfindAllByUser_theReturnListUserRole() {
        //When
        userRepository.save(user);
        roleRepository.save(role);
        userRoleRepository.save(userRole);
        Optional<List<UserRole>> userRoles = userRoleRepository.findAllByUser(user);

        //Then
        assertTrue(userRoles.isPresent());
        assertEquals("User", userRoles.get().getFirst().getUser().getName());
        assertEquals("user-test", userRoles.get().getFirst().getUser().getUsername());
        assertEquals("123456", userRoles.get().getFirst().getUser().getPassword());
        assertEquals("email@email.com", userRoles.get().getFirst().getUser().getEmail());

        assertEquals("ROLE_USER", userRoles.get().getFirst().getRole().getAuthority());
        assertEquals("USER", userRoles.get().getFirst().getRole().getName());
    }



}
package br.com.feedhub.unit.infrastructure.repository.security;

import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.security.UserRepository;
import br.com.feedhub.integration.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRespositoryTest extends AbstractIntegrationTest {

    @Autowired
    UserRepository repository;

    private User user;

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");
    }

    @Test
    @DisplayName("Test Given User Object when Save User then Return Saved User")
    void testGivenUserObject_whenSaveUser_theReturnSavedUser() {
        //When
        User savedUser = repository.save(user);

        //Then
        assertNotNull(savedUser);
        assertEquals("User", savedUser.getName());
        assertEquals("user-test", savedUser.getUsername());
        assertEquals("123456", savedUser.getPassword());
        assertEquals("email@email.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Test Given User Object when findByUsernameAndPassword User then Return Saved User")
    void testGivenUserObject_whenFindByUsernameAndPassword_theReturnSavedUser() {
        //When
        repository.save(user);
        Optional<User> foundUser = repository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        //Then
        assertTrue(foundUser.isPresent());
        assertEquals("User", foundUser.get().getName());
        assertEquals("user-test", foundUser.get().getUsername());
        assertEquals("123456", foundUser.get().getPassword());
        assertEquals("email@email.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Test Given User Object when findByUsername User then Return Saved User")
    void testGivenUserObject_whenFindByUsername_theReturnSavedUser() {
        //When
        repository.save(user);
        Optional<User> foundUser = repository.findByUsername(user.getUsername());

        //Then
        assertTrue(foundUser.isPresent());
        assertEquals("User", foundUser.get().getName());
        assertEquals("user-test", foundUser.get().getUsername());
        assertEquals("123456", foundUser.get().getPassword());
        assertEquals("email@email.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Test Given User Object when findByFilters User then Return User List")
    void testGivenUserObject_whenFindByFilters_theReturnUserList() {
        //When
        repository.save(user);
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<User> usersPage = repository.findAllByFilters("User", "user-test", pageable);

        //Then
        assertTrue(usersPage.getTotalElements() > 0);
        assertTrue(usersPage.getNumberOfElements() > 0);
        usersPage.forEach(Assertions::assertNotNull);


    }


}
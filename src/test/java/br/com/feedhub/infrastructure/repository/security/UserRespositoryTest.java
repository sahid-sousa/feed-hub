package br.com.feedhub.infrastructure.repository.security;

import br.com.feedhub.domain.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRespositoryTest {

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


}
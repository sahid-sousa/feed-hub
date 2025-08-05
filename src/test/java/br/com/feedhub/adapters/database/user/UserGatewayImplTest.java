package br.com.feedhub.adapters.database.user;

import br.com.feedhub.domain.security.User;
import br.com.feedhub.infrastructure.repository.security.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserGatewayImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserGatewayImpl userGateway;

    private User user;
    private Pageable pageable;

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "title");
        pageable = PageRequest.of(0, 10, sort);
    }

    @Test
    @DisplayName("Test Given User Object when Save User then Return Saved User")
    void testGivenUserObject_whenSaveUser_theReturnSavedUser() {
        //When
        given(userRepository.save(user)).willReturn(user);

        User savedUser = userGateway.save(user);

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
        given(userGateway.findByUsernameAndPassword(user.getUsername(), user.getPassword())).willReturn(Optional.of(user));
        Optional<User> foundUser = userGateway.findByUsernameAndPassword(user.getUsername(), user.getPassword());

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
        given(userGateway.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        Optional<User> foundUser = userGateway.findByUsername(user.getUsername());

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
        List<User> users = List.of(user);
        Page<User> expectedPage = new PageImpl<>(users, pageable, users.size());
        given(userGateway.findAllByFilters("User", "user-test", pageable)).willReturn(expectedPage);

        Page<User> usersPage = userGateway.findAllByFilters("User", "user-test", pageable);

        //The
        assertTrue(usersPage.hasContent());
        assertTrue(usersPage.getTotalElements() > 0);
        assertTrue(usersPage.getNumberOfElements() > 0);
        usersPage.forEach(Assertions::assertNotNull);
    }

}
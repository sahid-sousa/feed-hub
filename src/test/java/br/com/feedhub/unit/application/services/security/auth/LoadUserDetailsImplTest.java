package br.com.feedhub.unit.application.services.security.auth;

import br.com.feedhub.application.services.security.auth.LoadUserDetailsImpl;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class LoadUserDetailsImplTest {

    @InjectMocks
    LoadUserDetailsImpl loadUserDetailsImpl;

    @Mock
    UserDetailsService userDetailsService;

    private UserResponse userResponse;

    @BeforeEach
    public void setup() {
        RoleDto roleDto = new RoleDto("USER", "USER_ROLE");
        userResponse = new UserResponse(
                1L,
                "user",
                "user-test",
                "",
                true,
                true,
                true,
                true,
                "email@email.com",
                List.of(roleDto)
        );
    }

    @Test
    @DisplayName("Test should return UserResponse when LoadUserDetails service called")
    void testShouldReturnUserResponse_whenLoadUserDetailsServiceCalled() {
        //Given
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(userResponse);

        //When
        UserDetails response = loadUserDetailsImpl.loadByUsername(anyString());

        //Then
        assertNotNull(response);
        assertEquals("user-test", response.getUsername());
        assertTrue(response.isEnabled());
        assertTrue(response.isAccountNonExpired());
        assertTrue(response.isAccountNonLocked());
        assertTrue(response.isCredentialsNonExpired());
    }
}

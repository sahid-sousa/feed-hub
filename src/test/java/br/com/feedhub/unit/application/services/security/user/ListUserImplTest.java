package br.com.feedhub.unit.application.services.security.user;


import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.application.services.security.user.ListUserImpl;
import br.com.feedhub.domain.security.Role;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.dto.response.UserResponse;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ListUserImplTest {

    @InjectMocks
    private ListUserImpl listUserImpl;

    @Mock
    UserGateway userGateway;

    @Mock
    UserRoleGateway userRoleGateway;

    private User user;
    private UserRole userRole;
    private Pageable pageable;

    @BeforeEach
    public void setup() {
        //Given
        user = new User();
        user.setId(1L);
        user.setName("User");
        user.setUsername("user-test");
        user.setPassword("123456");
        user.setEmail("email@email.com");

        Role role = new Role();
        role.setId(1L);
        role.setName("USER");
        role.setAuthority("ROLE_USER");

        userRole = new UserRole();
        userRole.setId(1L);
        userRole.setRole(role);
        userRole.setUser(user);

        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "title");
        pageable = PageRequest.of(0, 10, sort);
    }

    @Test
    @DisplayName("Test receive params when ListUser then return PageListResponse")
    void testReceiveParams_whenListUser_thenReturnPageListResponse() {
        //Given
        given(userRoleGateway.findAllByUser(any())).willReturn(Optional.of(List.of(userRole)));
        List<User> users = List.of(user);
        Page<User> expectedPage = new PageImpl<>(users, pageable, users.size());
        given(userGateway.findAllByFilters(anyString(), anyString(), any())).willReturn(expectedPage);

        //When
        PageListResponse<UserResponse> response = listUserImpl.execute(
                "User",
                "user-test",
                0,
                10,
                "name",
                "asc"
        );

        //Then
        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(1, response.totalElements());
        assertEquals(10, response.size());
    }

}

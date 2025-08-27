package br.com.feedhub.unit.application.usecases.security.user;

import br.com.feedhub.application.usecases.security.user.CreateUser;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {

    @Mock
    CreateUser createUser;

    private UserResponse userResponse;

    @BeforeEach
    public void setup() {
        //Given
        userResponse = new UserResponse(
                1L,
                "User",
                "user-test",
                "",
                true,
                true,
                true,
                true,
                "email@email.com",
                List.of(new RoleDto("USER", "ROLE_USER"))
        );
    }

    @Test
    @DisplayName("Test receive UserCreateRequest when create then return UserResponse")
    void testReceiveUserCreateRequest_whenCreate_thenReturnUserResponse() {
        //Given
        given(createUser.execute(any())).willReturn(userResponse);

        //When
        UserResponse response = createUser.execute(any());
        //Then
        assertNotNull(response);
        assertEquals(userResponse.getId(), response.getId());
        assertEquals(userResponse.getName(), response.getName());
        assertEquals(userResponse.getEmail(), response.getEmail());
        assertEquals(userResponse.getPassword(), response.getPassword());
    }

}

package br.com.feedhub.unit.application.usecases.security.user;

import br.com.feedhub.application.usecases.security.user.UpdateUser;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTest {

    @Mock
    UpdateUser updateUser;

    private UserResponse userResponse;

    @BeforeEach
    public void setup() {
        //Given
        userResponse = new UserResponse(
                1L,
                "User-updated",
                "user-test-updated",
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
    @DisplayName("Test receive UserUpdateRequest when update then return UserResponse")
    void testReceiveUserUpdateRequest_whenUpdate_thenReturnUserResponse() {
        //Given
        given(updateUser.execute(any(), any())).willReturn(userResponse);

        //When
        UserResponse response = updateUser.execute(any(), any());

        //Then
        assertNotNull(response);
        assertEquals(userResponse.getName(), response.getName());
        assertEquals(userResponse.getEmail(), response.getEmail());
        assertEquals(userResponse.getEmail(), response.getEmail());
    }

}

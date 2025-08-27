package br.com.feedhub.unit.application.usecases.security.user;

import br.com.feedhub.application.usecases.security.user.FindUser;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FindUserTest {

    @Mock
    FindUser findUser;

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
                "email@email.com.br",
                List.of(new RoleDto("USER", "ROLE_USER"))
        );
    }


    @Test
    @DisplayName("Test Given User Object when findByUsername User then Return Saved User")
    void testReceiveLongId_whenFindUser_thenReturnUserResponse() {
        //Given
        given(findUser.execute(anyLong())).willReturn(userResponse);

        //When
        UserResponse response = findUser.execute(anyLong());

        //Then
        assertNotNull(response);
        assertEquals(userResponse.getId(), response.getId());
        assertEquals(userResponse.getUsername(), response.getUsername());
        assertEquals(userResponse.getEmail(), response.getEmail());
    }

}

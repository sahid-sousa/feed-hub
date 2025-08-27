package br.com.feedhub.unit.application.usecases.security.user;

import br.com.feedhub.application.usecases.security.user.ListUser;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ListUserTest {

    @Mock
    ListUser listUser;

    PageListResponse<UserResponse> pageListResponse;

    @BeforeEach
    public void setup() {
        //Given
        UserResponse userResponse = new UserResponse(
                1L,
                "User",
                "user-test",
                "",
                true,
                true,
                true,
                true,
                "email@email.com.br",
                List.of(new RoleDto("USER", "USER_ROLE"))
        );

        pageListResponse = new PageListResponse<>(
                List.of(userResponse),
                0,
                10,
                1L,
                1,
                true
        );
    }

    @Test
    @DisplayName("Test receive params when ListUser then return PageListResponse")
    void testReceiveParams_whenListUser_thenReturnPageListResponse() {
        //Given
        given(listUser.execute(
                "User",
                "user-test",
                0,
                10,
                "name",
                "asc"
        )).willReturn(pageListResponse);

        //When
        PageListResponse<UserResponse> response = listUser.execute(
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

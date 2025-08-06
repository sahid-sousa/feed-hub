package br.com.feedhub.interfaces.controllers.user;

import br.com.feedhub.application.usecases.security.user.CreateUser;
import br.com.feedhub.application.usecases.security.user.FindUser;
import br.com.feedhub.application.usecases.security.user.ListUser;
import br.com.feedhub.application.usecases.security.user.UpdateUser;
import br.com.feedhub.interfaces.dto.request.user.UserCreateRequest;
import br.com.feedhub.interfaces.dto.request.user.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CreateUser createUser;

    @Mock
    private UpdateUser updateUser;

    @Mock
    private FindUser findUser;

    @Mock
    private ListUser listUser;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserController userController;

    private UserCreateRequest userCreateRequest;
    private UserUpdateRequest userUpdateRequest;
    private UserResponse userCreateResponse;
    private UserResponse userUpdateResponse;

    @BeforeEach
    public void setup() {
        userCreateRequest = new UserCreateRequest("User", "user-test", "123456", "email@email.com");
        userCreateResponse = new UserResponse(1L, "User", "user-test", "", true, true, true, true, "email@email.com", List.of());
        userUpdateRequest = new UserUpdateRequest("User Updated", "246810", "email-update@email.com");
        userUpdateResponse = new UserResponse(1L, "User Updated", "user-test", "", true, true, true, true, "email-update@email.com", List.of());
    }

    @Test
    @DisplayName("Test Receive UserCreateRequest when Create User then Return UserResponse")
    void testReceiveUserCreateRequest_whenCreateUser_thenReturnUserResponse() {
        // Given
        given(createUser.execute(any(UserCreateRequest.class)))
                .willReturn(userCreateResponse);

        // When
        ResponseEntity<?> responseEntity = userController.create(userCreateRequest);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userCreateResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test Receive UserUpdateRequest when UpdateUser then Return UserResponse")
    void testReceiveUserUpdateRequest_whenUpdateUser_thenReturnUserResponse() {
        // Given
        given(updateUser.execute(any(UserUpdateRequest.class), any(HttpServletRequest.class)))
                .willReturn(userUpdateResponse);

        // When
        ResponseEntity<?> responseEntity = userController.update(userUpdateRequest, request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userUpdateResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test Receive Long whenFindUser then Return UserResponse")
    void testReceiveLong_whenFindUser_thenReturnUserResponse() {
        //Given
        given(findUser.execute(anyLong())).willReturn(userCreateResponse);

        //When
        ResponseEntity<?> responseEntity = userController.findById(1L);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userCreateResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test Receive Params whenList then Return PageListResponse")
    void testReceiveParams_whenList_thenResponse_thenReturnPageListResponse() {
        //Given
        var expectedPage = new PageListResponse<>(List.of(userCreateResponse), 0, 10, 1, 1, true);
        given(listUser.execute(anyString(), anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .willReturn(expectedPage);

        //When
        ResponseEntity<?> responseEntity = userController.list(
                "User",
                "user-test",
                0,
                10,
                "name",
                "asc"
        );

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPage, responseEntity.getBody());
    }

}
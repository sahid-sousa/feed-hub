package br.com.feedhub.unit.interfaces.controllers.security;

import br.com.feedhub.application.usecases.security.auth.Authenticate;
import br.com.feedhub.interfaces.controllers.security.AuthController;
import br.com.feedhub.interfaces.dto.request.security.AccountCredentials;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    Authenticate authenticate;

    @InjectMocks
    AuthController authController;

    private AccountCredentials accountCredentials;
    private TokenResponse token;

    @BeforeEach
    public void setup() {
        accountCredentials = new AccountCredentials("user-test", "123456");
        token = new TokenResponse("user-test", "", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Test Receive AccountCredentials when Authenticated then Return TokenResponse")
    public void testReceiveAccountCredentials_whenAuthenticated_thenReturnTokenResponse() {
        //Given
        given(authenticate.signin(any(AccountCredentials.class))).willReturn(token);

        //When
        ResponseEntity<?> responseEntity = authController.authenticate(accountCredentials);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(token, responseEntity.getBody());

    }


}
package br.com.feedhub.unit.application.usecases.security.auth;

import br.com.feedhub.application.usecases.security.auth.Authenticate;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthenticateTest {

    @Mock
    Authenticate authenticate;

    private TokenResponse tokenResponse;

    @BeforeEach
    public void setup() {
        tokenResponse = new TokenResponse(
                "user-test",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }


    @Test
    @DisplayName("Test should return Jwt token when login is successful")
    void testShouldReturnJwtToken_whenLoginIsSuccessful() {
        //Given
        given(authenticate.signin(any())).willReturn(tokenResponse);
        //When
        TokenResponse response = authenticate.signin(any());

        //Then
        assertNotNull(response);
        assertEquals("user-test", tokenResponse.username());
        assertEquals(tokenResponse, response);
    }
}

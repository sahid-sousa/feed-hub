package br.com.feedhub.unit.application.services.security.auth;

import br.com.feedhub.application.services.security.auth.RefreshTokenImpl;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenImplTest {

    @Mock
    RefreshTokenImpl refreshToken;

    TokenResponse tokenResponse;

    @BeforeEach
    public void setup() {
        //Given
        tokenResponse = new TokenResponse(
                "user-test",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Test Refresh JWT Token")
    void testRefreshJwtToken() {
        //Given
        given(refreshToken.execute(anyString(), anyString())).willReturn(tokenResponse);

        //When
        TokenResponse response = refreshToken.execute(anyString(), anyString());

        //Then
        assertNotNull(response);
        assertFalse(tokenResponse.token().isEmpty());
        assertEquals(224, tokenResponse.token().length());
    }

}

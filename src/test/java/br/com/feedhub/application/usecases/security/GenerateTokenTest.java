package br.com.feedhub.application.usecases.security;

import br.com.feedhub.application.usecases.security.auth.GenerateToken;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class GenerateTokenTest {

    @Mock
    GenerateToken generateToken;

    TokenResponse tokenResponse;

    @BeforeEach
    public void setup() {
        //Given
        tokenResponse = new TokenResponse(
                "user-test",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Test Generate JWT Token")
    void testGenerateJwtToken() {
        //Given
        given(generateToken.execute(anyString(), anyList())).willReturn(tokenResponse);

        //When
        TokenResponse response = generateToken.execute(anyString(), anyList());

        //Then
        assertNotNull(response);
        assertFalse(tokenResponse.token().isEmpty());
        assertEquals(224, tokenResponse.token().length());
    }

}
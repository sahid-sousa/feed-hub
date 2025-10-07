package br.com.feedhub.unit.application.services.security.auth;

import br.com.feedhub.application.services.security.auth.GenerateTokenImpl;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenerateTokenImplTest {

    @Mock
    GenerateTokenImpl jwtToken;

    String username;
    List<String> roles ;

    @BeforeEach
    public void setup() {
        //Given
        jwtToken = new GenerateTokenImpl(
                "4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P",
                3600000L,
                "feedhub"
        );

        username = "user-test";
        roles = List.of("ROLE_USER", "ROLE_ADMIN");
    }


    @Test
    @DisplayName("Test Generate JWT Token")
    void testGenerateJwtToken() {
        //When
        TokenResponse tokenResponse = jwtToken.execute("user-test", roles) ;

        System.out.println(tokenResponse.token());
        System.out.println(tokenResponse.refreshToken());

        //Then
        assertNotNull(tokenResponse);
        assertFalse(tokenResponse.token().isEmpty());
        assertFalse(tokenResponse.refreshToken().isEmpty());
        assertEquals(220, tokenResponse.token().length());
    }

}
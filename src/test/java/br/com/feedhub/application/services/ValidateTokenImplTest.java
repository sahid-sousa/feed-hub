package br.com.feedhub.application.services;

import br.com.feedhub.application.services.security.auth.GenerateTokenImpl;
import br.com.feedhub.application.services.security.auth.ValidateTokenImpl;
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
class ValidateTokenImplTest {

    @Mock
    GenerateTokenImpl jwtToken;
    @Mock
    ValidateTokenImpl tokenImpl;

    String username;
    List<String> roles;

    @BeforeEach
    public void setup() {

        jwtToken = new GenerateTokenImpl(
                "4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P",
                3600000L,
                "feedhub"
        );

        tokenImpl = new ValidateTokenImpl("4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P");

        username = "alirio-user";
        roles = List.of("ROLE_USEER", "ROLE_ADMIN");
    }

    @Test
    @DisplayName("Test Validate Generated Token")
    void testValidateGeneratedToken() {
        //When
        TokenResponse tokenResponse = jwtToken.generate("alirio-user", roles) ;
        Boolean isValidToken = tokenImpl.validate(tokenResponse.token());

        //Then
        assertTrue(isValidToken);
    }

}
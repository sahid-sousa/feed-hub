package br.com.feedhub.unit.application.usecases.security.auth;

import br.com.feedhub.application.usecases.security.auth.ValidateToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidateTokenTest {

    @Mock
    ValidateToken validateToken;

    @Test
    @DisplayName("Test Validate Generated Token")
    void testValidateGeneratedToken() {
        //Given
        given(validateToken.execute(anyString())).willReturn(true);

        //When
        Boolean result = validateToken.execute(anyString());

        //Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Test Validate Invalid Token")
    void testValidateInvalidToken() {
        //Given
        given(validateToken.execute(anyString())).willReturn(false);

        //When
        Boolean result = validateToken.execute(anyString());

        //Then
        assertFalse(result);
    }

}
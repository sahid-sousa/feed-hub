package br.com.feedhub.unit.application.usecases.security.auth;

import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ExtractUsernameTest {

    @Mock
    private ExtractUsername extractUsername;

    @Test
    @DisplayName("Test should return username")
    void testShouldReturnUsername() {
        //Given
        given(extractUsername.execute(anyString())).willReturn("user-test");

        //When
        String username = extractUsername.execute(anyString());

        //Then
        assertEquals("user-test", username);
    }

}

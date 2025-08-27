package br.com.feedhub.unit.application.usecases.security.auth;

import br.com.feedhub.application.usecases.security.auth.ResolveToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ResolveTokenTest {

    @Mock
    ResolveToken resolveToken;

    private String token;

    @BeforeEach
    public void setup() {
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
    }

    @Test
    @DisplayName("Test should return resolved token when token is provided")
    void testShouldReturnResolvedToken_whenTokenIsProvided() {
        //Given
        given(resolveToken.execute(anyString())).willReturn(token);

        //When
        String resolvedToken = resolveToken.execute(anyString());

        //Then
        assertEquals(resolvedToken, token);
    }

}

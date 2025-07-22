package br.com.feedhub.application.services.security;

import br.com.feedhub.application.services.security.auth.ResolveTokenImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResolveTokenImplTest {

    @Mock
    ResolveTokenImpl resolveToken;

    private String token;
    private String bearerToken;

    @BeforeEach
    public void setup() {
        //Given
        resolveToken = new ResolveTokenImpl();
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX1VTRUVSIiwiUk9MRV9BRE1JTiJdLCJpc3MiOiJmZWVkaHViIiwiaWF0IjoxNzUyMDM1NjQyLCJleHAiOjE3NTIwMzkyNDIsInN1YiI6ImFsaXJpby11c2VyIn0.-6HFVDjpD4yn-QHqc95a8p4NukSywt7NmISCBuBQbOQ";
        bearerToken =  "Bearer " + token;
    }

    @Test
    @DisplayName("Test Resolve JWT Token")
    void testResolveJwtToken() {
        //When
        String resolvedToken = resolveToken.resolve(bearerToken);

        //Then
        assertNotNull(resolvedToken);
        assertEquals(token, resolvedToken);
    }

}
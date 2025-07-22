package br.com.feedhub.application.usecases;

import br.com.feedhub.application.usecases.security.auth.GenerateToken;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenerateTokenTest {

    @Mock
    GenerateToken generateToken;

}
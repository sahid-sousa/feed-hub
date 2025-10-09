package br.com.feedhub.application.usecases.security.auth;

import br.com.feedhub.interfaces.dto.response.TokenResponse;

public interface RefreshToken {
    TokenResponse execute(String username, String refreshToken);
}

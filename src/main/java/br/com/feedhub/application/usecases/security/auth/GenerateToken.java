package br.com.feedhub.application.usecases.security.auth;

import br.com.feedhub.interfaces.dto.response.TokenResponse;

import java.util.List;

public interface GenerateToken {
    TokenResponse execute(String username, List<String> roles);
}

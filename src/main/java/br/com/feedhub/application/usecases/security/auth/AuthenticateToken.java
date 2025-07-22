package br.com.feedhub.application.usecases.security.auth;

import org.springframework.security.core.Authentication;

public interface AuthenticateToken {
    Authentication authenticate(String token);
}

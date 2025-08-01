package br.com.feedhub.application.usecases.security.auth;

public interface ValidateToken {
    Boolean execute(String token);
}

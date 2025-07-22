package br.com.feedhub.application.usecases.security.auth;

public interface ValidateToken {
    Boolean validate(String token);
}

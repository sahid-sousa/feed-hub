package br.com.feedhub.application.usecases.security.auth;

public interface ResolveToken {
    String execute(String token);
}

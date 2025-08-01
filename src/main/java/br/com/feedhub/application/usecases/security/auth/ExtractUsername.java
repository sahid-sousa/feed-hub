package br.com.feedhub.application.usecases.security.auth;

public interface ExtractUsername {
    String execute(String jwtToken);
}

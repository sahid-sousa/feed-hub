package br.com.feedhub.application.usecases.security.auth;

import br.com.feedhub.interfaces.dto.request.AccountCredentials;
import br.com.feedhub.interfaces.dto.response.TokenResponse;

public interface Authenticate {
    TokenResponse signin(AccountCredentials credentials);
}

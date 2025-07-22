package br.com.feedhub.application.usecases.security.auth;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface TokenDecoder {
    DecodedJWT decode(String token);
}

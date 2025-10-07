package br.com.feedhub.interfaces.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public record TokenResponse(
        String username,
        String token,
        String refreshToken,
        LocalDateTime creation,
        LocalDateTime expiration
) implements Serializable { }

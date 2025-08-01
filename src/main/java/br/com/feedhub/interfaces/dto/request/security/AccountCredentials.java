package br.com.feedhub.interfaces.dto.request.security;

import java.io.Serial;
import java.io.Serializable;

public record AccountCredentials(
        String username,
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public boolean hasNullOrEmptyFields() {
        return isNullOrEmpty(username) || isNullOrEmpty(password);
    }

    public boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

}

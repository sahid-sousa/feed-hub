package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.interfaces.dto.response.UserResponse;

public interface FindUser {
    UserResponse find(Long id);
}

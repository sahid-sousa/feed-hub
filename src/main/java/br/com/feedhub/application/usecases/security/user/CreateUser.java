package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.interfaces.dto.request.UserCreateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;

public interface CreateUser {
    UserResponse create(UserCreateRequest user);
}

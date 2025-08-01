package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.interfaces.dto.request.user.UserCreateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;

public interface CreateUser {
    UserResponse execute(UserCreateRequest user);
}

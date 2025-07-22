package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.interfaces.dto.request.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;

public interface UpdateUser {
    UserResponse update(UserUpdateRequest user);
}

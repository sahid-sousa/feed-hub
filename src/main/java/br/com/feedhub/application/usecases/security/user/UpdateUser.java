package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.interfaces.dto.request.user.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UpdateUser {
    UserResponse execute(UserUpdateRequest user, HttpServletRequest request);
}

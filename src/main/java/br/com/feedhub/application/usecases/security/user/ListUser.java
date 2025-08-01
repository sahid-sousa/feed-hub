package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.dto.response.UserResponse;

public interface ListUser {
   PageListResponse<UserResponse> execute(String name, String username, int page, int size, String sortBy, String sortDirection);
}

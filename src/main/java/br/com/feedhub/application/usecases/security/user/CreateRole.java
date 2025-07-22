package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.domain.security.Role;
import br.com.feedhub.interfaces.dto.request.BaseUserDetails;

import java.util.List;

public interface CreateRole {
    List<Role> create(BaseUserDetails baseUserDetails);
}

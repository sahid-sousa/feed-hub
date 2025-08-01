package br.com.feedhub.application.usecases.security.user;

import br.com.feedhub.domain.security.Role;

import java.util.List;

public interface CreateRole {
    List<Role> execute(String[] roles);
}

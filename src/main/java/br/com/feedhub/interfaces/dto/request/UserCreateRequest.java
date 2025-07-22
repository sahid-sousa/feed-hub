package br.com.feedhub.interfaces.dto.request;

import java.util.List;


public class UserCreateRequest extends BaseUserDetails {

    public UserCreateRequest() {
        super();
    }

    public UserCreateRequest(
            String name,
            String username,
            String password,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            boolean enabled,
            String email,
            List<RoleDto> authorities
    ) {
        super(name, username, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, email, authorities);
    }



}

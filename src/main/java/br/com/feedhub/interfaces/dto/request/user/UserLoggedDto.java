package br.com.feedhub.interfaces.dto.request.user;

import java.util.List;

public class UserLoggedDto extends BaseUserDetails {

    public UserLoggedDto() {
        super();
    }

    public UserLoggedDto(
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

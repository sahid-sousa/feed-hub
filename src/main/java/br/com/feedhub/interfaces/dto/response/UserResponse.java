package br.com.feedhub.interfaces.dto.response;

import br.com.feedhub.interfaces.dto.request.BaseUserDetails;
import br.com.feedhub.interfaces.dto.request.RoleDto;

import java.util.List;

public class UserResponse  extends BaseUserDetails {

    private Long id;

    public UserResponse(){
        super();
    }

    public UserResponse(
            Long id,
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
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

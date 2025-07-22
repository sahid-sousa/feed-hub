package br.com.feedhub.interfaces.dto.request;


import org.springframework.security.core.GrantedAuthority;

public class RoleDto implements GrantedAuthority {

    private String description;
    private String authority;

    public RoleDto() {}

    public RoleDto(String description, String authority) {
        this.description = description;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

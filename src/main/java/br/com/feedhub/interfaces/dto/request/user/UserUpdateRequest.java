package br.com.feedhub.interfaces.dto.request.user;

public class UserUpdateRequest {

    private String name;
    private String password;
    private String email;

    public UserUpdateRequest(){

    }

    public UserUpdateRequest(
            String name,
            String password,
            String email

    ) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

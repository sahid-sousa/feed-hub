package br.com.feedhub.utils;

import br.com.feedhub.interfaces.dto.request.BaseUserDetails;
import br.com.feedhub.interfaces.exceptions.PropertiesNotFoundException;
import br.com.feedhub.interfaces.exceptions.PropertiesNotValidException;
import org.springframework.stereotype.Component;

@Component
public class Validations {

    public void isNameUsernamePasswordEmailOrAuthoritiesEmpty(BaseUserDetails user) {
        boolean validation = isNameUsernamePasswordEmailEmpty(
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        ) ||  user.getAuthorities() == null || user.getAuthorities().isEmpty();
        if (validation) {
            throw new PropertiesNotFoundException("Attributes username, password, email or authorities are empty");
        }
    }

    public void isValidEmail(BaseUserDetails user) {
        boolean validation = isValidEmail(user.getEmail());
        if (!validation) {
            throw new PropertiesNotValidException("Attribute email: " + user.getEmail()  + " not valid");
        }
    }

    public boolean isNameUsernamePasswordEmailEmpty(
            String name,
            String username,
            String password,
            String email
    ) {
        return isNullOrEmpty(name) ||
                isNullOrEmpty(username) ||
                isNullOrEmpty(password) ||
                isNullOrEmpty(email);
    }

    public boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(EMAIL_REGEX);
    }


}

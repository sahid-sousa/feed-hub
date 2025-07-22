package br.com.feedhub.application.usecases.security.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface LoadUserDetails {
    UserDetails loadByUsername(String username);
}

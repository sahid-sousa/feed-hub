package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.application.usecases.security.auth.LoadUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoadUserDetailsImpl implements LoadUserDetails {

    private final UserDetailsService userDetailsService;

    public LoadUserDetailsImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserDetails loadByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }

}

package br.com.feedhub.adapters.security;

import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.adapters.database.user.UserRoleGateway;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.interfaces.dto.request.user.RoleDto;
import br.com.feedhub.interfaces.dto.request.user.UserLoggedDto;
import br.com.feedhub.utils.GenericBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityUserDetails implements UserDetailsService {

    UserGateway userGateway;
    UserRoleGateway userRoleGateway;

    public SecurityUserDetails(UserGateway userGateway, UserRoleGateway userRoleGateway) {
        this.userGateway = userGateway;
        this.userRoleGateway = userRoleGateway;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, SessionAuthenticationException {
        Optional<User> user = userGateway.findByUsername(username);
        if (user.isPresent()) {
            Optional<List<UserRole>> userRole = userRoleGateway.findAllByUser(user.get());
            List<RoleDto> permisions = new ArrayList<>();
            if (userRole.isPresent() && !userRole.get().isEmpty()) {
                List<UserRole> userRoles = userRole.get();
                userRoles.forEach( roles -> permisions.add(new RoleDto(roles.getRole().getName(), roles.getRole().getAuthority())));
                return GenericBuilder.of(UserLoggedDto::new)
                        .with(UserLoggedDto::setName, user.get().getName())
                        .with(UserLoggedDto::setUsername, user.get().getUsername())
                        .with(UserLoggedDto::setPassword, user.get().getPassword())
                        .with(UserLoggedDto::setAccountNonExpired, user.get().isAccountNonExpired())
                        .with(UserLoggedDto::setAccountNonLocked, user.get().isAccountNonLocked())
                        .with(UserLoggedDto::setCredentialsNonExpired, user.get().isCredentialsNonExpired())
                        .with(UserLoggedDto::setEnabled, user.get().isEnabled())
                        .with(UserLoggedDto::setEmail, user.get().getEmail())
                        .with(UserLoggedDto::setAuthorities,  permisions)
                        .build();
            } else {
                throw new SessionAuthenticationException(username + "without role");
            }
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}

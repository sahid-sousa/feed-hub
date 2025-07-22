package br.com.feedhub.adapters.database.user;

import br.com.feedhub.domain.security.User;
import br.com.feedhub.domain.security.UserRole;
import br.com.feedhub.infrastructure.repository.security.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleGatewayImpl implements UserRoleGateway {

    private final UserRoleRepository userRoleRepository;

    public UserRoleGatewayImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public void deleteAllByUser(User user) {
        userRoleRepository.deleteAllByUser(user);
    }

    @Override
    public Optional<List<UserRole>> findAllByUser(User user) {
        return userRoleRepository.findAllByUser(user);
    }

}

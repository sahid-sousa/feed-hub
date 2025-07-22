package br.com.feedhub.infrastructure.repository.security;

import br.com.feedhub.domain.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query("SELECT u FROM User u WHERE " +
            "(:name = '' OR u.name ILIKE CONCAT('%', :name, '%')) AND " +
            "(:username = '' OR u.username ILIKE CONCAT('%', :username, '%'))")
    Page<User> findAllByFilters(
            @Param("name") String name,
            @Param("username") String username,
            Pageable pageable
    );

}

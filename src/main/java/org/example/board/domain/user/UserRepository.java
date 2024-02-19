package org.example.board.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
//    Optional<User> findByEmail(String username);

    Optional<SiteUser> findByUsername(String username);

    Optional<SiteUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

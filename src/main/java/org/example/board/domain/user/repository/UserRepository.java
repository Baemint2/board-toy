package org.example.board.domain.user.repository;

import org.example.board.domain.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

    Optional<SiteUser> findByUsername(String username);


    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    void deleteById(Long id);


    @Query("SELECT username FROM SiteUser WHERE email = :email")
    Optional<String> findSiteUserByEmail(String email);

}

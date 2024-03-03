package org.example.board.domain.user.repository;

import org.example.board.domain.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

    @Query("SELECT u.username FROM SiteUser u WHERE u.username = :username")
    Optional<SiteUser> findByUsername(@Param("username") String username);


    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    void deleteById(Long id);

    @Query("SELECT u.username FROM SiteUser u WHERE u.email = :email AND u.nickname = :nickname")
    Optional<String> findByEmailAndNickname(@Param("email") String email,
                                            @Param("nickname") String nickname);

}

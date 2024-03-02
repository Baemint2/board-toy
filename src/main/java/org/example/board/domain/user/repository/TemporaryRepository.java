package org.example.board.domain.user.repository;

import org.example.board.domain.user.entity.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TemporaryRepository extends JpaRepository<TemporaryUser, Long> {
    Optional<TemporaryUser> findByEmail(String email);

    void findAllByExpiryDateTimeBefore(LocalDateTime now);
}

package org.example.board.domain.user.repository;

import org.example.board.domain.user.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmail(String email);

    List<EmailVerification> findAllByExpiryDateTimeBefore(LocalDateTime now);
}

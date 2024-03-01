package org.example.board.domain.email.repository;

import org.example.board.domain.email.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VerificationRepository extends JpaRepository<VerificationCode, Long> {

    List<VerificationCode> findAllByExpiryDateTimeBefore(LocalDateTime localDateTime);
}

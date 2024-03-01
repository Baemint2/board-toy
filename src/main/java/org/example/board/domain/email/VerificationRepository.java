package org.example.board.domain.email;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findBySiteUserEmail(String email);
}

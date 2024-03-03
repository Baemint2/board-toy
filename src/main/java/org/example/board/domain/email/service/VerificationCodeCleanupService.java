package org.example.board.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.email.entity.VerificationCode;
import org.example.board.domain.email.repository.VerificationRepository;
import org.example.board.domain.user.entity.EmailVerification;
import org.example.board.domain.user.repository.EmailVerificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationCodeCleanupService {
    private final VerificationRepository verificationRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredVerificationCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<VerificationCode> expiredCodes = verificationRepository.findAllByExpiryDateTimeBefore(now);
        verificationRepository.deleteAll(expiredCodes);
        cleanupExpiredTemporaryUsers(now);

    }

    public void cleanupExpiredTemporaryUsers(LocalDateTime now) {
        List<EmailVerification> expiredCodes = emailVerificationRepository.findAllByExpiryDateTimeBefore(now);
        emailVerificationRepository.deleteAll(expiredCodes);

    }

}

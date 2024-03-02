package org.example.board.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.email.entity.VerificationCode;
import org.example.board.domain.email.repository.VerificationRepository;
import org.example.board.domain.user.repository.TemporaryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationCodeCleanupService {
    private final VerificationRepository verificationRepository;
    private final TemporaryRepository temporaryRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredVerificationCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<VerificationCode> expiredCodes = verificationRepository.findAllByExpiryDateTimeBefore(now);
        verificationRepository.deleteAll(expiredCodes);
        cleanupExpiredTemporaryUsers(now);

    }

    public void cleanupExpiredTemporaryUsers(LocalDateTime now) {
        temporaryRepository.findAllByExpiryDateTimeBefore(now);

    }

}

package org.example.board.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.email.entity.VerificationCode;
import org.example.board.domain.email.repository.VerificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationCodeCleanupService {
    private VerificationRepository verificationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredVerificationCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<VerificationCode> expiredCodes = verificationRepository.findAllByExpiryDateTimeBefore(now);
        verificationRepository.deleteAll(expiredCodes);
    }
}

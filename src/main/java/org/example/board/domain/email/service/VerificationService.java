package org.example.board.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.entity.EmailVerification;
import org.example.board.domain.user.repository.EmailVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final EmailVerificationRepository emailVerificationRepository;

    //인증 번호 검증 로직
    public boolean verifyCode(String email, String code) {
        Optional<EmailVerification> optionalTemporaryUser = emailVerificationRepository.findByEmail(email);
        if (optionalTemporaryUser.isPresent()) {
            EmailVerification emailVerification = optionalTemporaryUser.get();
            if (code.equals(emailVerification.getVerificationCode()) &&
                    LocalDateTime.now().isBefore(emailVerification.getExpiryDateTime())) {
                emailVerificationRepository.delete(emailVerification); // 임시 데이터 삭제
                return true;
            }
        }
        return false;

    }
}

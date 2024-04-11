package org.example.board.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.entity.EmailVerification;
import org.example.board.domain.user.repository.EmailVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.board.common.utils.VerificationCodeUtils.generateVerificationCode;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;

    //인증번호 전송 및 인증테이블에 저장
    public void sendAndSaveVerificationCode(String email) {
        Optional<EmailVerification> existingUser = emailVerificationRepository.findByEmail(email);

        existingUser.ifPresent(emailVerificationRepository::delete);

        String verificationCode = generateVerificationCode();
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .expiryDateTime(LocalDateTime.now().plusMinutes(5))
                .build();

        emailVerificationRepository.save(emailVerification);

        emailService.sendVerificationEmail(email, verificationCode);
    }
}

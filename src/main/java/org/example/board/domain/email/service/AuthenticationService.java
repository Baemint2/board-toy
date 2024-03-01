package org.example.board.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.entity.TemporaryUser;
import org.example.board.domain.user.repository.TemporaryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.board.config.VerificationCodeUtils.generateVerificationCode;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TemporaryRepository temporaryRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void generateAndSendVerificationCode(String email, String username, String nickname ,String password) {

        Optional<TemporaryUser> existingUser = temporaryRepository.findByEmail(email);

        existingUser.ifPresent(temporaryRepository::delete);

        String verificationCode = generateVerificationCode(); // 인증 코드 생성 메소드
        String encodedPassword = passwordEncoder.encode(password);
        TemporaryUser temporaryUser = TemporaryUser.builder()
                .email(email)
                .username(username)
                .nickname(nickname)
                .password(encodedPassword)
                .verificationCode(verificationCode)
                .expiryDateTime(LocalDateTime.now().plusMinutes(5))
                .build();

        temporaryRepository.save(temporaryUser);

        emailService.sendVerificationEmail(email, verificationCode); // 이메일 전송 로직
    }
}

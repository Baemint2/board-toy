package org.example.board.domain.email.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.email.dto.EmailVerificationDto;
import org.example.board.domain.email.service.AuthenticationService;
import org.example.board.domain.email.service.VerificationService;
import org.example.board.domain.user.entity.EmailVerification;
import org.example.board.domain.user.repository.EmailVerificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailApiController {

    private final AuthenticationService authenticationService;
    private final VerificationService verificationService;
    private final EmailVerificationRepository emailVerificationRepository;
    // 인증 코드 전송
    @PostMapping("/api/email/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@Valid @RequestBody EmailVerificationDto verificationDto) {
        String email = verificationDto.getEmail();

        try {
            authenticationService.sendAndSaveVerificationCode(email);
            return ResponseEntity.ok().body(Map.of("message", "인증번호가 전송되었습니다."));
        } catch (Exception ex) {
            log.error("인증번호 전송 중 오류 발생: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "인증번호 전송 중 오류가 발생했습니다."));
        }
    }

    // 인증 코드 검증
    @PostMapping("/api/email/verifyCode")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody EmailVerificationDto verificationDto, HttpSession session) {
        String email = verificationDto.getEmail();
        String code = verificationDto.getCode();
        log.info("인증 코드 검증 요청: email={}, code={}", email, code);
        try {
            Optional<EmailVerification> optionalTemporaryUser = emailVerificationRepository.findByEmail(email);
            if (optionalTemporaryUser.isPresent()) {
                EmailVerification emailVerification = optionalTemporaryUser.get();
                log.info("인증 정보: email={}, savedCode={}, expiryDateTime={}",
                        emailVerification.getEmail(), emailVerification.getVerificationCode(), emailVerification.getExpiryDateTime());
            } else {
                log.info("인증 이메일 정보 없음: email={}", email);
            }

            boolean isCodeValid = verificationService.verifyCode(email, code);
            if (isCodeValid) {
                log.info("인증 성공: email={}", email);
                session.setAttribute("verifiedEmail", email);
                return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
            } else {
                log.info("인증 실패: email={}", email);
                return ResponseEntity.badRequest().body(Map.of("error", "인증 실패"));
            }
        } catch (Exception ex) {
            log.error("인증 코드 검증 중 오류 발생: email={}, error={}", email, ex.getMessage(), ex);
            return ResponseEntity.internalServerError().body(Map.of("error", "인증 코드 검증 중 오류가 발생했습니다."));
        }
    }
}

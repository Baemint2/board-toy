package org.example.board.domain.email.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.user.VerificationService;
import org.example.board.domain.user.entity.TemporaryUser;
import org.example.board.domain.user.repository.TemporaryRepository;
import org.example.board.domain.user.service.AuthenticationService;
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
    private final TemporaryRepository temporaryRepository;
    // 인증 코드 전송
    @PostMapping("/api/email/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String username = request.get("username");
        String nickname = request.get("nickname");
        String password = request.get("password");

        try {
            authenticationService.generateAndSendVerificationCode(email, username, nickname, password);
            return ResponseEntity.ok().body(Map.of("message", "인증번호가 전송되었습니다."));
        } catch (Exception ex) {
            log.error("인증번호 전송 중 오류 발생: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "인증번호 전송 중 오류가 발생했습니다."));
        }
    }

    // 인증 코드 검증
    @PostMapping("/api/email/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        log.info("인증 코드 검증 요청: email={}, code={}", email, code);
        try {
            Optional<TemporaryUser> optionalTemporaryUser = temporaryRepository.findByEmail(email);
            if (optionalTemporaryUser.isPresent()) {
                TemporaryUser temporaryUser = optionalTemporaryUser.get();
                log.info("임시 사용자 정보: email={}, savedCode={}, expiryDateTime={}",
                        temporaryUser.getEmail(), temporaryUser.getVerificationCode(), temporaryUser.getExpiryDateTime());
            } else {
                log.info("임시 사용자 정보 없음: email={}", email);
            }

            boolean isCodeValid = verificationService.verifyCode(email, code);
            if (isCodeValid) {
                log.info("인증 성공: email={}", email);
                return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
            } else {
                log.info("인증 실패: email={}", email);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "인증 실패"));
            }
        } catch (Exception ex) {
            log.error("인증 코드 검증 중 오류 발생: email={}, error={}", email, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "인증 코드 검증 중 오류가 발생했습니다."));
        }
    }
}

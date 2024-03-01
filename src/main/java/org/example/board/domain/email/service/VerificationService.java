package org.example.board.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.entity.TemporaryUser;
import org.example.board.domain.user.repository.TemporaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final TemporaryRepository temporaryRepository;

    //인증 번호 검증 로직
    public boolean verifyCode(String email, String code) {
        Optional<TemporaryUser> optionalTemporaryUser = temporaryRepository.findByEmail(email);
        if (optionalTemporaryUser.isPresent()) {
            TemporaryUser temporaryUser = optionalTemporaryUser.get();
            if (code.equals(temporaryUser.getVerificationCode()) &&
                    LocalDateTime.now().isBefore(temporaryUser.getExpiryDateTime())) {
                temporaryRepository.delete(temporaryUser); // 임시 데이터 삭제
                return true;
            }
        }
        return false;

    }
}

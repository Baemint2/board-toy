package org.example.board.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String verificationCode;

    private LocalDateTime expiryDateTime;

    @Builder
    public EmailVerification(String email, String verificationCode, LocalDateTime expiryDateTime) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.expiryDateTime = expiryDateTime;
    }

}

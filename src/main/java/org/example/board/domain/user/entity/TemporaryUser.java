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
public class TemporaryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String verificationCode;

    private String username;

    private String nickname;

    private String password;

    private LocalDateTime expiryDateTime;

    @Builder
    public TemporaryUser(String email, String verificationCode, LocalDateTime expiryDateTime,
                         String username, String nickname, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.verificationCode = verificationCode;
        this.expiryDateTime = expiryDateTime;
    }

}

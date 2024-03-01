package org.example.board.domain.email.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.user.entity.SiteUser;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private LocalDateTime expiryDateTime; // 만료 시간

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SiteUser siteUser;

    @Builder
    public VerificationCode(String code, LocalDateTime expiryDateTime, SiteUser siteUser) {
        this.code = code;
        this.expiryDateTime = expiryDateTime;
        this.siteUser = siteUser;
    }
}

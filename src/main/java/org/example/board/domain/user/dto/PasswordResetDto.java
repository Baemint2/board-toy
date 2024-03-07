package org.example.board.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.user.entity.SiteUser;

@Getter
@NoArgsConstructor
public class PasswordResetDto {

    private String username;

    @NotEmpty(message = "변경될 패스워드를 입력해주세요.")
    private String newPassword;

    @NotEmpty(message = "비밀번호를 확인 해주세요.")
    @Pattern(regexp="^[a-zA-Z0-9!@#$%^&*()_+]{6,}$", message="비밀번호는 6자 이상의 알파벳, 숫자, 특정 특수문자만 가능합니다.")
    private String confirmPassword;

    @NotEmpty(message = "현재 비밀번호를 입력해주세요.")
    @Pattern(regexp="^[a-zA-Z0-9!@#$%^&*()_+]{6,}$", message="비밀번호는 6자 이상의 알파벳, 숫자, 특정 특수문자만 가능합니다.")
    private String currentPassword;

    @Builder
    public PasswordResetDto(SiteUser siteUser, String newPassword, String confirmPassword) {
        this.username = siteUser.getUsername();
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.currentPassword = siteUser.getPassword();
    }

}

package org.example.board.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.board.domain.user.SiteUser;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class UserCreateDto {

    @Pattern(regexp = "^[a-z0-9_-]{5,20}$", message = "사용자명은 5자~20자 사이의 알파벳 소문자, 숫자, 특수문자(_),(-)만 가능합니다.")
    private String username;

    @Pattern(regexp="^[a-zA-Z0-9!@#$%^&*()_+]{6,}$", message="비밀번호는 6자 이상의 알파벳, 숫자, 특정 특수문자만 가능합니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인해주세요.")
    private String password2;

    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @Builder
    public UserCreateDto(String username, String password1, String password2, String email) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.email = email;
    }

    public SiteUser toEntity(PasswordEncoder passwordEncoder) {
        return SiteUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password1))
                .build();
    }


}

package org.example.board.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
    @Size(min=3, max=20)
    @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
    private String password2;

    @Email
    @NotEmpty(message = "이메일은 필수 항목입니다.")
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

package org.example.board.domain.user.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameUpdateDto {

    @NotEmpty(message = "공백문자는 사용할 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,}", message = "닉네임은 2자 이상의 알파벳 대소문자, 숫자, 한글만 가능합니다.")
    private String nickname;

    public NicknameUpdateDto(String nickname) {
        this.nickname = nickname;
    }
}

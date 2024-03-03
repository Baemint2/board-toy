package org.example.board.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.user.entity.SiteUser;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String email;
    private String nickname;

    @Builder
    public UserResponseDto(SiteUser siteUser) {
        this.username = siteUser.getUsername();
        this.email = siteUser.getEmail();
        this.nickname = siteUser.getNickname();
    }
}

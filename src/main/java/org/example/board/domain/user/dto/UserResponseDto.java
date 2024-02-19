package org.example.board.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.user.Role;
import org.example.board.domain.user.SiteUser;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String email;
    private String username;
    private Role role;

    @Builder
    public UserResponseDto(SiteUser siteUser) {
        this.email = siteUser.getEmail();
        this.username = siteUser.getUsername();
        this.role = siteUser.getRole();
    }
}

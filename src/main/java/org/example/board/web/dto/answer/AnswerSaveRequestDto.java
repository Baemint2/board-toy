package org.example.board.web.dto.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.board.domain.answer.Answer;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.user.SiteUser;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AnswerSaveRequestDto {
    @NotEmpty(message = "댓글 내용은 필수입니다.")
    private String content;
    private Long postId;
    private Long siteUserId;

    @Builder
    public AnswerSaveRequestDto(String content, Long postId, Long siteUserId) {
        this.content = content;
        this.postId = postId;
        this.siteUserId = siteUserId;
    }

    public Answer toEntity(SiteUser siteUser, Posts posts) {
        return Answer.builder()
                .content(content)
                .siteUser(siteUser)
                .posts(posts)
                .build();
    }
}

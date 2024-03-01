package org.example.board.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.answer.entity.Answer;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.user.entity.SiteUser;

@Getter
@NoArgsConstructor
public class AnswerSaveRequestDto {

    @NotBlank(message = "댓글 내용은 필수입니다.")
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

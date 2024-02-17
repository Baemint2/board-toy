package org.example.board.domain.posts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {

    private Long id;

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(min=3, max=150)
    private String title;

    @Size(max=1000)
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    private String author;
//    private Long fileId;

    @Builder
    public PostsUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
//        this.fileId = entity.getFileId();
    }

}

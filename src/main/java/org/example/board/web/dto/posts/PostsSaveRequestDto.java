package org.example.board.web.dto.posts;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.board.domain.posts.Posts;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(min=3, max=150)
    private String title;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
//        this.fileId = fileId;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
//                .fileId(fileId)
                .build();
    }

}
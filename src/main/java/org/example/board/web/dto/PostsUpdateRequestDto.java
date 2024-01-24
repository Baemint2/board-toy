package org.example.board.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.posts.Posts;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {

    private String title;
    private String content;

    public PostsUpdateRequestDto(Posts entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
    }
}

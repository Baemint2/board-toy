package org.example.board.domain.posts.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.posts.entity.Posts;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsSearchDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int viewCount;
    private int likeCount;

    @Builder
    public PostsSearchDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.viewCount = entity.getViewCount();
        this.likeCount = entity.getLikeCount();
    }
}

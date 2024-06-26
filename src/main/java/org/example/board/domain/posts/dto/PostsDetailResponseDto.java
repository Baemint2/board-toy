package org.example.board.domain.posts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsDetailResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int viewCount;
    private int answerCount;
    private int likeCount;
    private String category;
    public  PostsDetailResponseDto(Long id, String title, String content, String author,
                                   LocalDateTime createDate, LocalDateTime modifiedDate,
                                   int viewCount, int answerCount, int likeCount, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDate = createDate;
        this.modifiedDate = modifiedDate;
        this.viewCount = viewCount;
        this.answerCount = answerCount;
        this.likeCount = likeCount;
        this.category = category;
    }
}

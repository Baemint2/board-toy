package org.example.board.domain.posts.dto;

import lombok.Getter;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.image.entity.Image;
import org.example.board.domain.posts.entity.Category;
import org.example.board.domain.posts.entity.Posts;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<AnswerResponseDto> answerList;
    private int viewCount;
    private int likeCount;
    private Category category;
    private List<Image> images;

    public PostsResponseDto(Posts entity, List<AnswerResponseDto> answerList) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.viewCount = entity.getViewCount();
        this.likeCount = entity.getLikeCount();
        this.category = entity.getCategory();
        this.images = entity.getImages();
        this.answerList = answerList;
    }

    public static PostsResponseDto of(Posts entity, List<AnswerResponseDto> answerList) {
        return new PostsResponseDto(entity, answerList);
    }
}

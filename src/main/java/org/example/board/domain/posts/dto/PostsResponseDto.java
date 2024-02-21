package org.example.board.domain.posts.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import org.example.board.domain.answer.Answer;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.answer.dto.AnswerResponseDto;

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
    public PostsResponseDto(Posts entity, List<AnswerResponseDto> answerList) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.viewCount = entity.getViewCount();
        this.answerList=answerList;
    }

}

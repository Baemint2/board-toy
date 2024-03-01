package org.example.board.domain.posts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.posts.entity.Category;
import org.example.board.domain.posts.entity.Posts;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(min=3, max=150)
    private String title;

    @Size(max=1000)
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    private String author;

    private Category category;

    private List<String> imageUrls;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author, Category category, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.imageUrls = imageUrls;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .likeCount(0)
                .category(category)
                .build();
    }

}
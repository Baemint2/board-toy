package org.example.board.domain.posts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.posts.Category;

import java.util.List;

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

    @NotEmpty(message = "카테고리를 선택해주세요")
    private String category;

    private List<String> imageUrls;

    public PostsUpdateRequestDto(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

}

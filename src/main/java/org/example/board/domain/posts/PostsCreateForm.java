package org.example.board.domain.posts;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostsCreateForm {

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max=150)
    private String title;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

}

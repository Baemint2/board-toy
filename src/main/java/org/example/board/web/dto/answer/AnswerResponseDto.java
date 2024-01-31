package org.example.board.web.dto.answer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.answer.Answer;

@Getter
@NoArgsConstructor
public class AnswerResponseDto {

    private Long id;
    private String content;
    private String author;

    public AnswerResponseDto(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.author = answer.getSiteUser().getUsername();
    }
}

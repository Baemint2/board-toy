package org.example.board.domain.answer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerUpdateRequestDto {

    private String content;

    @Builder
    public AnswerUpdateRequestDto(String content) {
        this.content=content;
    }

}

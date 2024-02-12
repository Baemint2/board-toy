package org.example.board.domain.answer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.answer.Answer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class AnswerResponseDto {

    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private List<AnswerResponseDto> children;

    public AnswerResponseDto(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.author = answer.getSiteUser().getUsername();
        this.createdDate = answer.getCreatedDate();
        this.children = answer.getChildren().stream()
                                .map(AnswerResponseDto::new)
                                .collect(Collectors.toList());
    }
}

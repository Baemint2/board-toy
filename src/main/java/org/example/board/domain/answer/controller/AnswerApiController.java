package org.example.board.domain.answer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.answer.AnswerService;
import org.example.board.domain.answer.dto.AnswerSaveRequestDto;
import org.example.board.domain.answer.dto.AnswerUpdateRequestDto;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnswerApiController {

    private final AnswerService answerService;

    //댓글 등록
    @PostMapping("/api/v1/answer/{id}")
    public Long save(@PathVariable Long id, AnswerSaveRequestDto saveRequestDto) {
        return answerService.saveAnswer(id, saveRequestDto);
    }

    //댓글 업데이트
    @PutMapping("/api/v1/answer/{id}")
    public Long update(@PathVariable Long id, @RequestBody AnswerUpdateRequestDto requestDto) {
        log.info("Updating answer with id: {}, request: {}", id, requestDto);
        return  answerService.update(id, requestDto);
    }

    //댓글 삭제
    @DeleteMapping("/api/v1/answer/{id}")
    public Long delete(@PathVariable Long id) {
        answerService.delete(id);
        return id;
    }


}

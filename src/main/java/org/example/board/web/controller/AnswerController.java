package org.example.board.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.board.service.AnswerService;
import org.example.board.service.PostsService;
import org.example.board.service.UserService;
import org.example.board.web.dto.answer.AnswerResponseDto;
import org.example.board.web.dto.answer.AnswerSaveRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {

    private final PostsService postsService;
    private final AnswerService answerService;
    private final UserService userService;

    // 댓글 등록
    @PostMapping("/create/{id}")
    public String createAnswer(@PathVariable Long id, Model model,
                               @RequestParam(value = "content") String content,
                               Principal principal) {
        Long siteUserId = userService.findUserIdByUsername(principal.getName());
        AnswerSaveRequestDto requestDto = new AnswerSaveRequestDto(content, id, siteUserId);
        answerService.saveAnswer(requestDto);
        return String.format("redirect:/posts/detail/%s", id);
    }

    // 댓글 수정
    @GetMapping("/update/{id}")
    public String updateAnswer(@PathVariable Long id, Model model) {
        AnswerResponseDto answerDto = answerService.findById(id);
        model.addAttribute("answer", answerDto);
        return "answer-update";
    }
}

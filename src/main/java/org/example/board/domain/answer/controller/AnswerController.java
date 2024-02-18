package org.example.board.domain.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.answer.AnswerService;
import org.example.board.domain.posts.PostsService;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.answer.dto.AnswerSaveRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {

    private final AnswerService answerService;
    private final UserService userService;

    // 댓글 등록
    @PostMapping("/create/{id}")
    public String createAnswer(@PathVariable Long id,
                               @RequestParam(value = "content") String content,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {

        SiteUser siteUserId = userService.findUserIdByUsername(principal.getName());
        AnswerSaveRequestDto requestDto = AnswerSaveRequestDto.builder()
                .postId(id)
                .siteUserId(siteUserId.getId())
                .content(content)
                .build();
        answerService.saveAnswer(id, requestDto);

        redirectAttributes.addFlashAttribute("message", "댓글이 성공적으로 등록되었습니다.");
        return String.format("redirect:/posts/detail/%s", id);
    }

    // 댓글 수정
    @PostMapping("/update/{id}")
    public String updateAnswer(@PathVariable Long id, Model model) {
        AnswerResponseDto answerDto = answerService.findById(id);
        model.addAttribute("answer", answerDto);
        return "answer-update";
    }
}

package org.example.board.domain.answer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.answer.dto.AnswerSaveRequestDto;
import org.example.board.domain.answer.service.AnswerService;
import org.example.board.domain.user.entity.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
                               RedirectAttributes redirectAttributes
                               ) {

        if(content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("answerError", "댓글 내용을 입력해주세요.");
            return String.format("redirect:/posts/detail/%s", id);
        }

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
}

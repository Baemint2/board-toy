package org.example.board.domain.posts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.board.domain.answer.Answer;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.answer.dto.AnswerSaveRequestDto;
import org.example.board.domain.posts.PostsService;
import org.example.board.domain.posts.dto.PostsUpdateRequestDto;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.example.board.domain.posts.dto.PostsResponseDto;
import org.example.board.domain.posts.dto.PostsSaveRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;
    private final UserService userService;
//    private final FileService fileService;

    @GetMapping("/posts/save")
    public String postsSave(Model model, Principal principal) {

        if(principal != null) {
            String loggedUser = principal.getName();
            model.addAttribute("loggedUser", loggedUser);
        }
        // 빈 객체를 모델에 추가
        model.addAttribute("requestDto", new PostsSaveRequestDto());
        return "posts-save";
    }

    @PostMapping("/posts/save")
    public String postsSave(@Valid PostsSaveRequestDto requestDto,
                            BindingResult bindingResult,
                            Principal principal,
                            Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("requestDto", requestDto);
            return "posts-save";
        }

        // loggedUser 는 현재 홈페이지에 로그인한 사용자 정보.
        SiteUser siteUser = userService.getUser(principal.getName());
        // 유효성 검사를 통과한 경우에만 create 메서드 호출
        postsService.create(requestDto, siteUser);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model,
                              Principal principal) {
        PostsResponseDto dto = postsService.findById(id);
        if(!dto.getAuthor().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        model.addAttribute("post", dto);
        return "posts-update";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, @Valid PostsUpdateRequestDto requestDto,
                              BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", requestDto);
            return "posts-update"; // 검증 오류가 있는 경우, 업데이트 폼으로 다시 리턴
        }

        postsService.update(id, requestDto, principal.getName());
        return String.format("redirect:/posts/detail/%s", id); // 성공적으로 업데이트된 경우, 상세 페이지로 리다이렉션
    }

    @GetMapping("/posts/detail/{id}")
    public String postsDetail(@PathVariable Long id, Model model, @ModelAttribute("message") String message) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("dto", dto);
        model.addAttribute("answer", new AnswerResponseDto());
        if (!message.isEmpty()) {
            model.addAttribute("message", message);
        }
        return "posts-detail";
    }
}

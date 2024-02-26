package org.example.board.domain.posts.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.answer.AnswerService;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.image.dto.ImageResponseDto;
import org.example.board.domain.image.service.ImageService;
import org.example.board.domain.posts.Category;
import org.example.board.domain.posts.PostsService;
import org.example.board.domain.posts.dto.PostsResponseDto;
import org.example.board.domain.posts.dto.PostsSaveRequestDto;
import org.example.board.domain.posts.dto.PostsUpdateRequestDto;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;
    private final AnswerService answerService;
    private final UserService userService;
    private final ImageService imageService;
    @GetMapping("/posts/save")
    public String postsSave(Model model, Principal principal) {

        if(principal != null) {
            String loggedUser = principal.getName();
            model.addAttribute("loggedUser", loggedUser);
        }
        model.addAttribute("categories", Arrays.asList(Category.values()));
        // 빈 객체를 모델에 추가
        model.addAttribute("requestDto", new PostsSaveRequestDto());
        return "posts-save";
    }

    @PostMapping("/posts/save")
    public String postsSave(@Valid PostsSaveRequestDto requestDto,
                            BindingResult bindingResult,
                            Principal principal,
                            Model model,
                            @RequestParam("images") List<MultipartFile> files) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("categories", Arrays.asList(Category.values()));
            model.addAttribute("requestDto", requestDto);
            return "posts-save";
        }

        // loggedUser 는 현재 홈페이지에 로그인한 사용자 정보.
        SiteUser siteUser = userService.getUser(principal.getName());
        // 유효성 검사를 통과한 경우에만 create 메서드 호출
        postsService.create(requestDto, siteUser, files);

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
        model.addAttribute("categories", Arrays.asList(Category.values()));
        model.addAttribute("post", dto);
        return "posts-update";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, @Valid PostsUpdateRequestDto requestDto,
                              BindingResult bindingResult, Principal principal, Model model,
                              @RequestParam("images") List<MultipartFile> files) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", requestDto);
            return "posts-update"; // 검증 오류가 있는 경우, 업데이트 폼으로 다시 리턴
        }

        Long update = postsService.update(id, requestDto, principal.getName(), files);
        log.info("게시글 수정 = {}", update);
        return String.format("redirect:/posts/detail/%s", id); // 성공적으로 업데이트된 경우, 상세 페이지로 리다이렉션
    }

    @GetMapping("/posts/detail/{id}")
    public String postsDetail(@PathVariable Long id,
                              Model model,
                              @ModelAttribute("message") String message,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Principal principal) {
        SiteUser siteUser = userService.getUser(principal.getName());
        PostsResponseDto dto = postsService.findById(id);
        ImageResponseDto image = imageService.findImage(siteUser.getUsername());
        List<AnswerResponseDto> answerImages = answerService.getAnswerImages(id);

        model.addAttribute("dto", dto);
        model.addAttribute("answer", new AnswerResponseDto());
        model.addAttribute("image", image);
        model.addAttribute("answerImages", answerImages);

        postsService.viewCountValidation(id, request, response);
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }
        return "posts-detail";
    }
}

package org.example.board.domain.posts.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.answer.service.AnswerService;
import org.example.board.domain.image.dto.ImageResponseDto;
import org.example.board.domain.image.service.ImageService;
import org.example.board.domain.posts.dto.PostsResponseDto;
import org.example.board.domain.posts.dto.PostsSaveRequestDto;
import org.example.board.domain.posts.entity.Category;
import org.example.board.domain.posts.service.PostsService;
import org.example.board.domain.user.entity.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/posts/save")
    public String postsSave(Model model, Principal principal) {

        if(principal != null) {
            String loggedUser = principal.getName();
            model.addAttribute("loggedUser", loggedUser);
        }
        model.addAttribute("categories", Arrays.asList(Category.values()));
        // 빈 객체를 모델에 추가
        model.addAttribute("requestDto", new PostsSaveRequestDto());
        return "post/posts-save";
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
        return "post/posts-update";
    }

    @GetMapping("/posts/detail/{id}")
    public String postsDetail(@PathVariable Long id,
                              Model model,
                              @ModelAttribute("message") String message,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Principal principal) {
        if(principal != null) {
            SiteUser siteUser = userService.getUser(principal.getName());
            ImageResponseDto image = imageService.findImage(siteUser.getUsername());
            model.addAttribute("loggedUser", siteUser.getUsername());
            model.addAttribute("image", image);
        }

        PostsResponseDto dto = postsService.findById(id);
        List<AnswerResponseDto> answerImages = answerService.getAnswerImages(id);
        boolean isUserLogin = principal != null;

        model.addAttribute("dto", dto);
        model.addAttribute("answer", new AnswerResponseDto());
        model.addAttribute("answerImages", answerImages);
        model.addAttribute("isUserLogin", isUserLogin);

        postsService.viewCountValidation(id, request, response);
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }

        return "post/posts-detail";
    }
}

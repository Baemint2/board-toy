package org.example.board.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.PostsCreateForm;
import org.example.board.domain.posts.PostsRepository;
import org.example.board.domain.user.SiteUser;
import org.example.board.service.PostsService;
import org.example.board.service.UserService;
import org.example.board.web.dto.PostsResponseDto;
import org.example.board.web.dto.PostsSaveRequestDto;
import org.example.board.web.dto.PostsUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;
    private final UserService userService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            String loggedUser = principal.getName();
            model.addAttribute("loggedUser", loggedUser);
        }
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }
}

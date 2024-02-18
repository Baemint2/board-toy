package org.example.board.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.image.service.ImageService;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.PostsService;
import org.example.board.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping("/")
    public String index(Model model, Principal principal,
                        @RequestParam(value = "page", defaultValue = "0")int page) {
        if (principal != null) {
            String loggedUser = principal.getName();
            model.addAttribute("loggedUser", loggedUser);
        }

        Page<Posts> paging = postsService.getList(page);
        model.addAttribute("paging", paging);
        return "index";
    }
}

package org.example.board.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.config.auth.JwtService;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.posts.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final PostsService postsService;
    private final JwtService jwtService;

    @GetMapping("/")
    public String index(Principal principal,
                        Model model, @RequestParam(value = "page", defaultValue = "0")int page) {
        if (principal != null) {
                String loggedUser = principal.getName();
                model.addAttribute("loggedUser", loggedUser);
                model.addAttribute("isLogin", true);
            } else {
            model.addAttribute("isLogin", false);
        }

        Page<Posts> paging = postsService.getList(page);
        model.addAttribute("paging", paging);
        return "index";
    }
}

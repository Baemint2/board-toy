package org.example.board.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.board.service.PostsService;
import org.example.board.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

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

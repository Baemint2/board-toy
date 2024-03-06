package org.example.board.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.posts.dto.PostsDetailResponseDto;
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

    @GetMapping("/")
    public String index(@RequestParam(value = "page", defaultValue = "0")int page,
                        @RequestParam(value = "type", required = false) String type,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        Principal principal,
                        Model model) {
        if (principal != null) {
                String loggedUser = principal.getName();
                model.addAttribute("loggedUser", loggedUser);
            }


        Page<Posts> paging;
        if(type != null && keyword != null && !type.isEmpty() && !keyword.isEmpty()) {
            Page<PostsDetailResponseDto> searchResult = postsService.searchPosts(type, keyword, page);

            model.addAttribute("paging", searchResult);
        } else {
            paging = postsService.getList(page);
            model.addAttribute("paging", paging);
        }
        return "index";
    }
}

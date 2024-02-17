package org.example.board.domain.posts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.board.domain.posts.PostsService;
import org.example.board.domain.posts.dto.PostsResponseDto;
import org.example.board.domain.posts.dto.PostsSaveRequestDto;
import org.example.board.domain.posts.dto.PostsUpdateRequestDto;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@Valid @RequestBody PostsSaveRequestDto requestDto) throws IOException {
        return postsService.save(requestDto);
    }

    //수정
    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody PostsUpdateRequestDto requestDto, Principal principal) {
        return postsService.update(id, requestDto, principal.getName());
    }
    //특정 글 조회
    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    //삭제
    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }

}

package org.example.board.web.apiController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.board.service.PostsService;
import org.example.board.web.dto.posts.PostsResponseDto;
import org.example.board.web.dto.posts.PostsSaveRequestDto;
import org.example.board.web.dto.posts.PostsUpdateRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@Valid @RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    //수정
    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
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

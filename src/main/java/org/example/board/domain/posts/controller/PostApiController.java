package org.example.board.domain.posts.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.posts.dto.PostsDetailResponseDto;
import org.example.board.domain.posts.dto.PostsResponseDto;
import org.example.board.domain.posts.dto.PostsSaveRequestDto;
import org.example.board.domain.posts.dto.PostsUpdateRequestDto;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.posts.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostApiController {

    private final PostsService postsService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts")
    public ResponseEntity<Long> save(@Valid @RequestPart("post") PostsSaveRequestDto requestDto,
                                     @RequestPart(value = "files", required = false)List<MultipartFile> files) throws IOException {
        Long savedPostId = postsService.save(requestDto, files);
        return ResponseEntity.ok(savedPostId);
    }

    //수정
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/posts/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id,
                       @Valid @RequestPart("post") PostsUpdateRequestDto requestDto, Principal principal,
                       @RequestPart(value = "files", required = false)List<MultipartFile> files) {
        Long updatedPostId = postsService.update(id, requestDto, principal.getName(), files);
        return ResponseEntity.ok(updatedPostId);
    }
    //특정 글 조회
    @GetMapping("/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    //삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }

    // 내가 작성한 글
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/posts")
    public ResponseEntity<List<Posts>> getUserPosts() {
        List<Posts> userPosts = postsService.findPostsByCurrentUser();
        return ResponseEntity.ok(userPosts);

    }

    // 조회수 증가
    @GetMapping("/posts/{postId}/increaseViewCount")
    public ResponseEntity<Integer> increaseViewCount(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response) {
        int updatedViewCount = postsService.viewCountValidation(postId, request, response);
        return ResponseEntity.ok(updatedViewCount);
    }

    // 최신순 게시글
    @GetMapping("/posts/latest/desc")
    public ResponseEntity<Page<PostsDetailResponseDto>> getPostsSortedByDesc(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<PostsDetailResponseDto> postsSortedByDesc = postsService.getPostsSortedByDesc(page);
        return ResponseEntity.ok(postsSortedByDesc);
    }

    //댓글 순
    @GetMapping("/posts/answer/desc")
    public ResponseEntity<Page<PostsDetailResponseDto>> getAnswerDesc(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<PostsDetailResponseDto> postsSortedByAnswerDesc = postsService.getPostsSortedByAnswerDesc(page);
        return ResponseEntity.ok(postsSortedByAnswerDesc);
    }

    //조회수 순
    @GetMapping("/posts/viewCount/desc")
    public ResponseEntity<Page<PostsDetailResponseDto>> getViewCountDesc(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<PostsDetailResponseDto> postsSortedByViewCountDesc = postsService.getPostsSortedByViewCountDesc(page);
        return ResponseEntity.ok(postsSortedByViewCountDesc);
    }

    //좋아요 순
    @GetMapping("/posts/like/desc")
    public ResponseEntity<Page<PostsDetailResponseDto>> getLikeCountDesc(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<PostsDetailResponseDto> postsSortedByLikeCountDesc = postsService.getPostsSortedByLikeCountDesc(page);
        return ResponseEntity.ok(postsSortedByLikeCountDesc);
    }

    // 좋아요 개수 확인
    @GetMapping("/posts/{postId}/likes/count")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long postId) {
        long likesCount = postsService.getLikesCount(postId);
        return ResponseEntity.ok(likesCount);
    }

    // 검색
    @GetMapping("/posts/search")
    public ResponseEntity<Page<PostsDetailResponseDto>> searchPosts(@RequestParam("type") String type,
                                                            @RequestParam("keyword") String keyword,
                                                            @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<PostsDetailResponseDto> searchResult = postsService.searchPosts(type, keyword, page);
        return ResponseEntity.ok(searchResult);
    }


}

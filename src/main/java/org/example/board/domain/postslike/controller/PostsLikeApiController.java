package org.example.board.domain.postslike.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.posts.dto.PostsResponseDto;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.postslike.service.PostsLikeService;
import org.example.board.domain.user.entity.SiteUser;
import org.example.board.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/")
public class PostsLikeApiController {

    private final PostsLikeService postsLikeService;
    private final UserRepository userRepository; // 사용자 정보 조회

    // 좋아요
    @PostMapping("{postId}/like")
    public ResponseEntity<?> addLike(@PathVariable Long postId, Authentication authentication) {
        return toggleLikeHandler(postId, authentication, true);
    }

    // 좋아요 취소
    @DeleteMapping("{postId}/like")
    public ResponseEntity<?> removeLike(@PathVariable Long postId,
                                        Authentication authentication) {
        return toggleLikeHandler(postId, authentication, false);
    }

    // 사용자가 특정 게시물에 좋아요를 눌렀는지 여부 확인
    @GetMapping("{postId}/like/status")
    public ResponseEntity<Map<String, Boolean>> checkLikeStatus(@PathVariable Long postId, Authentication authentication) {
        try {
            String username = authentication.getName();
            SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자명입니다."));

            boolean isLiked = postsLikeService.checkLike(postId, siteUser.getId());
            Map<String, Boolean> response = new HashMap<>();
            response.put("isLiked", isLiked);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            log.error("존재하지 않는 사용자명: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("예상치 못한 오류가 발생했습니다: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

    }

    //좋아요 누른 글 확인
    @GetMapping("/likes")
    public ResponseEntity<List<PostsResponseDto>> getLikedPosts(Authentication authentication) {
        try {
            String username = authentication.getName();
            SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자명 입니다."));
            List<Posts> likedPosts = postsLikeService.findLikePostsByUser(siteUser.getId());
            List<PostsResponseDto> responseDto = likedPosts.stream()
                    .map(posts -> {
                        List<AnswerResponseDto> answerDto = posts.getAnswerList()
                                .stream().map(AnswerResponseDto::new).toList();
                        return new PostsResponseDto(posts, answerDto);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDto);
        } catch (UsernameNotFoundException e) {
            log.error("존재하지 않는 사용자명: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("오류가 발생했습니다: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    private ResponseEntity<?> toggleLikeHandler(Long postId, Authentication authentication, boolean isToggle) {
        try{
            String username = authentication.getName();
            SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자명입니다."));

            log.info("Toggling like: postId={}, isToggle={}, username={}", postId, isToggle, username);
            if(isToggle) {
                postsLikeService.addLike(postId, siteUser.getId());
            } else {
                postsLikeService.removeLike(postId, siteUser.getId());
            }
            // 성공 응답 반환
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException e) {
            log.error("존재하지 않는 사용자명: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package org.example.board.domain.postslike.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.posts.repository.PostsRepository;
import org.example.board.domain.postslike.entity.PostsLike;
import org.example.board.domain.postslike.repository.PostsLikeRepository;
import org.example.board.domain.user.entity.SiteUser;
import org.example.board.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsLikeService {

    private final PostsLikeRepository postsLikeRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;


    // 좋아요
    @Transactional
    public void addLike(Long postId, Long userId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));
        SiteUser siteUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        if (!postsLikeRepository.existsByPostsIdAndSiteUserId(postId, userId)) {
            PostsLike postsLike = PostsLike.builder()
                    .posts(post)
                    .siteUser(siteUser)
                    .likedAt(LocalDateTime.now())
                    .build();
            postsLikeRepository.save(postsLike);
            post.addCount();
            log.info("Like added: postId={}, userId={}", postId, userId);
        } else {
            log.info("Like already exists: postId={}, userId={}", postId, userId);
        }
    }

    // 좋아요 취소
    @Transactional
    public void removeLike(Long postId, Long userId) {
        if (postsLikeRepository.existsByPostsIdAndSiteUserId(postId, userId)) {
            postsLikeRepository.deleteByPostsIdAndSiteUserId(postId, userId);
            Posts post = postsRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));
            post.removeCount();
            log.info("Like removed: postId={}, userId={}", postId, userId);
        } else {
            log.info("Like does not exist: postId={}, userId={}", postId, userId);
        }
    }

    //좋아요 체크 확인
    @Transactional(readOnly = true)
    public boolean checkLike(Long postId, Long userId) {
        return postsLikeRepository.existsByPostsIdAndSiteUserId(postId, userId);

    }

    @Transactional(readOnly = true)
    public List<Posts> findLikePostsByUser(Long userId) {
        return postsLikeRepository.findPostsLikeBySiteUserId(userId);
    }

}

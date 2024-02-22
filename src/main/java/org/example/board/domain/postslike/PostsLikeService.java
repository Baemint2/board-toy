package org.example.board.domain.postslike;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.PostsRepository;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsLikeService {

    private final PostsLikeRepository postsLikeRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

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

    @Transactional(readOnly = true)
    public boolean checkLike(Long postId, Long userId) {
        return postsLikeRepository.existsByPostsIdAndSiteUserId(postId, userId);

    }

}

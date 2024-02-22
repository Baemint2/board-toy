package org.example.board.domain.postslike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostsLikeRepository extends JpaRepository<PostsLike, Long> {

    // 게시글 좋아요 여부 확인
    boolean existsByPostsIdAndSiteUserId(Long postId, Long userId);

    // 특정 게시글 좋아요 수 조회
    long countByPostsId(Long postId);

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM PostsLike pl WHERE pl.posts.id = :postId AND pl.siteUser.id = :userId")
    void deleteByPostsIdAndSiteUserId(Long postId, Long userId);
}

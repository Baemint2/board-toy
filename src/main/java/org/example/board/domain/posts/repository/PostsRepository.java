package org.example.board.domain.posts.repository;

import org.example.board.domain.posts.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long>, QueryRepository, QueryPostsRepository {


    Page<Posts> findAll(Pageable pageable);

    List<Posts> findByAuthorOrderByCreatedDateDesc(String author);
    //좋아요 수
    @Query("SELECT COUNT(p1) FROM PostsLike p1 where p1.posts.id = :postId")
    long countById(@Param("postId")Long postId);

}
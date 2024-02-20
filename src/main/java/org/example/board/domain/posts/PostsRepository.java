package org.example.board.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

    Page<Posts> findAll(Pageable pageable);

    List<Posts> findByAuthor(String author);

//    @Modifying
//    @Query("update Posts p set p.PostsView = p.PostsView + 1 where p.id = :id")
//    Long updateView(@Param("id") Long id);

}
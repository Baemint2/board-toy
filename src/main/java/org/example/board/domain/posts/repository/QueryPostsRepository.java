package org.example.board.domain.posts.repository;

import org.example.board.domain.posts.dto.PostsDetailResponseDto;
import org.example.board.domain.posts.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryPostsRepository{
    Page<PostsDetailResponseDto> searchByKeyword(String type, String keyword, Pageable pageable);
}

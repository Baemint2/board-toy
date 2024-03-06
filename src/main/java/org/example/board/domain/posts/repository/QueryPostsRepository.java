package org.example.board.domain.posts.repository;

import org.example.board.domain.posts.dto.PostsDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryPostsRepository{
    Page<PostsDetailResponseDto> searchByKeyword(String type, String keyword, Pageable pageable);
}

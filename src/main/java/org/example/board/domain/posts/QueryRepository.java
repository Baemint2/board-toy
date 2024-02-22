package org.example.board.domain.posts;

import org.example.board.domain.posts.dto.PostsDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryRepository {
    Page<Posts> findAllByLatest(Pageable pageable);

    Page<PostsDetailResponseDto> findAllOrderByAnswerCountDesc(Pageable pageable);

    Page<PostsDetailResponseDto> findAllOrderByViewCountDesc(Pageable pageable);
}

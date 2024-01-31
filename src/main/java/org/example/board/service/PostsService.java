package org.example.board.service;


import lombok.RequiredArgsConstructor;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.PostsRepository;
import org.example.board.domain.user.SiteUser;
import org.example.board.web.dto.answer.AnswerResponseDto;
import org.example.board.web.dto.posts.PostsListResponseDto;
import org.example.board.web.dto.posts.PostsResponseDto;
import org.example.board.web.dto.posts.PostsSaveRequestDto;
import org.example.board.web.dto.posts.PostsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    // 글 등록
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    // 글 화면에 등록?
    public Posts create(PostsSaveRequestDto requestDto,SiteUser user) {
        Posts posts = Posts.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(requestDto.getAuthor())
                .build();
        postsRepository.save(posts);
        return posts;
    }

    // 글 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다." + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    //특정 글 조회
    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다." + id));

        List<AnswerResponseDto> answerDto = entity.getAnswerList().stream()
                .map(AnswerResponseDto::new)
                .collect(Collectors.toList());

                return new PostsResponseDto(entity, answerDto);
    }
    // 글 조회 id 내림차순
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 글 삭제
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다." + id));

        postsRepository.delete(posts);
    }


}

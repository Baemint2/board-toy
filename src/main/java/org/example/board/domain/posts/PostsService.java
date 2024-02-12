package org.example.board.domain.posts;


import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.posts.dto.PostsListResponseDto;
import org.example.board.domain.posts.dto.PostsResponseDto;
import org.example.board.domain.posts.dto.PostsSaveRequestDto;
import org.example.board.domain.posts.dto.PostsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    // 글 등록
    public Long save(PostsSaveRequestDto requestDto){
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


    //페이징 처리
    public Page<Posts> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.postsRepository.findAll(pageable);
    }

    // 조회수
//    @Transactional
//    public Long updateView(Long id) {
//        return this.postsRepository.updateView(id);
//    }


}

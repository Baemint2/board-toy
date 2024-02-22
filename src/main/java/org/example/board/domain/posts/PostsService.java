package org.example.board.domain.posts;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.posts.dto.*;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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
                .author(user.getUsername())
                .build();
        return postsRepository.save(posts);
    }

    // 글 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto, String username) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다." + id));

        if(!posts.getAuthor().equals(username)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
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

    //현재 로그인한 사용자가 작성한 글 조회
    public List<Posts> findPostsByCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return postsRepository.findByAuthorOrderByCreatedDateDesc(username);
    }

    //조회수
    @Transactional
    public void viewCountValidation(Long postId, HttpServletRequest request,
                                     HttpServletResponse response) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + postId));
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;
        //Request에 쿠기가 있을 때
        for (int i = 0; cookies != null && i < cookies.length ; i++) {
            if(cookies[i].getName().equals("postView")) {
                cookie = cookies[i];

                if(!cookie.getValue().contains("[" + posts.getId() + "]")) {
                    posts.addViewCount();
                    cookie.setValue(cookie.getValue() + "[" + posts.getId() + "]");
                }
                isCookie = true;
                break;
            }
        }
        if(!isCookie) {
            posts.addViewCount();
            cookie = new Cookie("postView", "[" + posts.getId() + "]");
        }

        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/");
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }

    @Transactional
    public Page<Posts> getPostsSortedByDesc(Pageable pageable) {
        return postsRepository.findAllByLatest(pageable);
    }
    @Transactional
    public Page<PostsDetailResponseDto> getPostsSortedByAnswerDesc(Pageable pageable) {
        return postsRepository.findAllOrderByAnswerCountDesc(pageable);
    }
    @Transactional
    public Page<PostsDetailResponseDto> getPostsSortedByViewCountDesc(Pageable pageable) {
        return postsRepository.findAllOrderByViewCountDesc(pageable);
    }

    @Transactional
    public long getLikesCount(Long postId) {
        return postsRepository.countById(postId);
    }


}

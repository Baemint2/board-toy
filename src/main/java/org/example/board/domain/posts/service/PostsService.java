package org.example.board.domain.posts.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.image.entity.Image;
import org.example.board.domain.image.service.ImageService;
import org.example.board.domain.posts.dto.*;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.posts.repository.PostsRepository;
import org.example.board.domain.user.entity.SiteUser;
import org.example.board.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Transactional
    // 글 등록
    public Long save(PostsSaveRequestDto requestDto, List<MultipartFile> files){
        Posts posts = requestDto.toEntity();
        Posts savedPosts = postsRepository.save(posts);
        List<Image> images = imageService.uploadPosts(files, savedPosts.getId());
        savedPosts.addImages(images);
        postsRepository.save(savedPosts);

        return savedPosts.getId();
    }

    @Transactional
    // 글 화면에 등록?
    public Posts create(PostsSaveRequestDto requestDto, SiteUser user, List<MultipartFile> files) {
        List<Image> images = imageService.uploadPosts(files, requestDto.toEntity().getId());

        Posts posts = Posts.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(user.getUsername())
                .images(images)
                .category(requestDto.getCategory())
                .build();
        return postsRepository.save(posts);
    }

    // 글 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto, String username, List<MultipartFile> files) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        // 게시글 정보 업데이트
        posts.update(requestDto);

        if(!posts.getAuthor().equals(username)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        // 파일 처리
        if (!files.isEmpty()) {
            List<Image> images = imageService.uploadPosts(files, posts.getId());
            posts.addImages(images);
        }

        return posts.getId();
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
    public int viewCountValidation(Long postId, HttpServletRequest request,
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
//            response.addCookie(cookie);
        }

        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/");
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);

        return posts.getViewCount();
    }

    //최신 순
    @Transactional
    public Page<PostsDetailResponseDto> getPostsSortedByDesc(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postsRepository.findAllByLatest(pageable);
    }

    //댓글 순
    @Transactional
    public Page<PostsDetailResponseDto> getPostsSortedByAnswerDesc(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("answerCount"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postsRepository.findAllOrderByAnswerCountDesc(pageable);
    }

    //조회수 순
    @Transactional
    public Page<PostsDetailResponseDto> getPostsSortedByViewCountDesc(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("viewCount"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postsRepository.findAllOrderByViewCountDesc(pageable);
    }

    //좋아요 순
    @Transactional
    public Page<PostsDetailResponseDto> getPostsSortedByLikeCountDesc(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("likeCount"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postsRepository.findAllOrderByLikeCountDesc(pageable);
    }

    @Transactional
    public long getLikesCount(Long postId) {
        return postsRepository.countById(postId);
    }

    // 검색
    public Page<PostsDetailResponseDto> searchPosts(String type, String keyword, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        return postsRepository.searchByKeyword(type, keyword, PageRequest.of(page, 10, Sort.by(sorts)));
    }

}

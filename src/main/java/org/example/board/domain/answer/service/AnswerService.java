package org.example.board.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.answer.dto.AnswerSaveRequestDto;
import org.example.board.domain.answer.dto.AnswerUpdateRequestDto;
import org.example.board.domain.answer.entity.Answer;
import org.example.board.domain.answer.repository.AnswerRepository;
import org.example.board.domain.image.entity.Image;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.posts.repository.PostsRepository;
import org.example.board.domain.user.entity.SiteUser;
import org.example.board.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveAnswer(Long postId, AnswerSaveRequestDto requestDto, String username) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. ID =" + requestDto.getPostId()));

        SiteUser siteUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("작성자가 존재하지 않습니다. ID =" + requestDto.getSiteUserId()));

        Answer answer = Answer.builder()
                .content(requestDto.getContent())
                .siteUser(siteUser)
                .posts(posts)
                .build();

        answerRepository.save(answer);
        return answer.getId();
    }
    @Transactional
    public Long update(Long id, AnswerUpdateRequestDto requestDto) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다." + id));

        answer.update(requestDto.getContent());
        return id;
    }

    @Transactional
    public void delete(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. " + id));

        answerRepository.delete(answer);
    }

    public AnswerResponseDto findById(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. " + id));

        return new AnswerResponseDto(answer);
    }

    public List<Posts> findPostsContentByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return answerRepository.findPostsContentByUser(username);
    }

    //댓글 프로필 사진
    public List<AnswerResponseDto> getAnswerImages(Long postId) {
        List<Answer> answers = answerRepository.findByPostsId(postId);
        List<AnswerResponseDto> answerDto = new ArrayList<>();

        for(Answer answer : answers) {
            SiteUser user = answer.getSiteUser();
            Image profileImage = user.getImage();
            String imageUrl;

            if(profileImage != null) {
                imageUrl = profileImage.getUrl();
            }
            else  {
                imageUrl = "/profiles/anonymous.png";
            }

            AnswerResponseDto dto = new AnswerResponseDto(answer, imageUrl);
            answerDto.add(dto);
        }

        return answerDto;
    }
}
package org.example.board.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.answer.Answer;
import org.example.board.domain.answer.AnswerRepository;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.PostsRepository;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.UserRepository;
import org.example.board.web.dto.answer.AnswerResponseDto;
import org.example.board.web.dto.answer.AnswerSaveRequestDto;
import org.example.board.web.dto.answer.AnswerUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveAnswer(AnswerSaveRequestDto requestDto) {
        Posts posts = postsRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. ID =" + requestDto.getPostId()));

        SiteUser siteUser = userRepository.findById(requestDto.getSiteUserId())
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
        Answer answer = answerRepository.findById(requestDto.getAnswerId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다." + id));

        answer.update(requestDto.getContent());
        return requestDto.getAnswerId();
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
}

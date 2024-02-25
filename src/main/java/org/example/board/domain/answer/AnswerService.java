package org.example.board.domain.answer;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.repository.PostsRepository;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.UserRepository;
import org.example.board.domain.answer.dto.AnswerResponseDto;
import org.example.board.domain.answer.dto.AnswerSaveRequestDto;
import org.example.board.domain.answer.dto.AnswerUpdateRequestDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveAnswer(Long id, AnswerSaveRequestDto requestDto) {
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
}

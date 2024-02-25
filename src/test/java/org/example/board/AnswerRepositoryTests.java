package org.example.board;

import org.example.board.domain.answer.AnswerRepository;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.repository.PostsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;

@SpringBootTest
public class AnswerRepositoryTests {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void testJpa() {
        Optional<Posts> op = this.postsRepository.findById(12L);
        assertTrue(op.isPresent());


    }
}

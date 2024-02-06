package org.example.board;

import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.PostsRepository;
import org.example.board.domain.posts.PostsService;
import org.example.board.domain.posts.dto.PostsSaveRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class PostsRepositoryTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostsService postsService;

    @Test
    void testPosts() throws  Exception{
            for (int i = 1; i <= 300; i++) {
                String title = "title";
                String content = "content";
                PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                        .title(title)
                        .content(content)
                        .author("작성자")
                        .build();
            }
    }

    @Test
    void testJpa() throws Exception{
        String title = "title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("작성자")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(2).getTitle()).isEqualTo(title);
        assertThat(all.get(2).getContent()).isEqualTo(content);
    }


}

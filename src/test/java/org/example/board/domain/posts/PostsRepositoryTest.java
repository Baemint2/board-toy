package org.example.board.domain.posts;

import org.example.board.domain.posts.entity.Category;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.posts.repository.PostsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @Test
    void testSavePost() {
        //Given
        Posts posts = Posts.builder()
                .title("테스트 제목")
                .content("테스트 내용입니다. 카테고리는 잘 들어갔나요?")
                .author("test1")
                .category(Category.SMALLTALK)
                .build();
        //When
        Posts savedPost = postsRepository.save(posts);

        //Then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getCategory()).isEqualTo(Category.SMALLTALK);
    }
}

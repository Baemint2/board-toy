package org.example.board.domain.posts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.common.BaseTimeEntity;
import org.example.board.deserializer.CategoryDeserializer;
import org.example.board.domain.answer.Answer;
import org.example.board.domain.image.Image;
import org.example.board.domain.posts.dto.PostsUpdateRequestDto;
import org.example.board.domain.postslike.PostsLike;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @JsonManagedReference
    private String author;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int likeCount;

    @JsonIgnore
    @Column(name = "View_Count", nullable = false, columnDefinition = "integer default 0")
    private int viewCount = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "posts",cascade = CascadeType.ALL)
    private List<PostsLike> postsList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @JsonDeserialize(using = CategoryDeserializer.class)
    private Category category;

    @JsonManagedReference("postToImage")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    //좋아요 증가
    public void addCount() {
        this.likeCount++;
    }

    //좋아요 취소
    public void removeCount() {
        this.likeCount--;
    }

    public void addViewCount() {
        this.viewCount+= 1;
    }

    public void addImages(List<Image> newImages) {
        for (Image image : newImages) {
            this.images.add(image);
            image.setPosts(this);
        }
    }

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @Builder
    public Posts(String title, String content, String author, int viewCount, int likeCount, Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.category = category;
        this.images = images;
    }

    public void update(PostsUpdateRequestDto requestDto) {
        if(!this.title.equals(requestDto.getTitle()) || !this.content.equals(requestDto.getContent())) {
            this.title = requestDto.getTitle();
            this.content = requestDto.getContent();
            this.category = Category.valueOf(requestDto.getCategory());
            this.modifiedDate = LocalDateTime.now();
        }
    }
}

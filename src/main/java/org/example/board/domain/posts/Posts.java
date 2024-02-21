package org.example.board.domain.posts;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.common.BaseTimeEntity;
import org.example.board.domain.answer.Answer;
import org.example.board.domain.postslike.PostsLike;

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

    private int likeCount;

    @Column(name = "View_Count", nullable = false, columnDefinition = "integer default 0")
    private int viewCount = 0;

    @OneToMany(mappedBy = "posts",cascade = CascadeType.ALL)
    private List<PostsLike> postsList = new ArrayList<>();

    //게시글 조회수
//    @Column(columnDefinition = "Long default 0", nullable = false)
//    private Long PostsView;

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


    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @Builder
    public Posts(String title, String content, String author, int viewCount) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.viewCount = viewCount;
    }

    public void update(String title, String content) {
        this.title=title;
        this.content=content;
    }
}

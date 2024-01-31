package org.example.board.domain.posts;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.board.domain.BaseTimeEntity;
import org.example.board.domain.answer.Answer;

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

    @Column(nullable = false)
    private String content;

    private String author;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

//    @Column
//    private Long fileId;

    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title=title;
        this.content=content;
    }
}

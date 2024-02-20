package org.example.board.domain.answer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.common.BaseTimeEntity;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.user.SiteUser;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor

public class Answer extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name="posts_id")
    @JsonBackReference
    private Posts posts;




    @Builder
    public Answer(String content, SiteUser siteUser, Posts posts) {
        this.content = content;
        this.siteUser = siteUser;
        this.posts = posts;


    }


    public void update(String content) {
        this.content = content;
    }


}

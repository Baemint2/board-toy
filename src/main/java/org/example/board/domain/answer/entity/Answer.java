package org.example.board.domain.answer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.common.BaseTimeEntity;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.user.entity.SiteUser;
import org.springframework.data.jpa.repository.EntityGraph;

@Getter
@Entity
@NoArgsConstructor

public class Answer extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonBackReference
    private SiteUser siteUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
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

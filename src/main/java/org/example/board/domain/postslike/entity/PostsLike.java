package org.example.board.domain.postslike.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.user.entity.SiteUser;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PostsLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "posts_id")
    public Posts posts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public SiteUser siteUser;

    private LocalDateTime likedAt;

    @Builder
    public PostsLike(Posts posts, SiteUser siteUser, LocalDateTime likedAt) {
        this.posts = posts;
        this.siteUser = siteUser;
        this.likedAt = likedAt;
    }
}

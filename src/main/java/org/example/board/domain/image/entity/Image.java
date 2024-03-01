package org.example.board.domain.image.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.posts.entity.Posts;
import org.example.board.domain.user.entity.SiteUser;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_user_ID")
    private SiteUser siteUser;

    @JsonBackReference("postToImage")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts post;

    @Builder
    public Image(String url, SiteUser siteUser, Posts post) {
        this.url = url;
        this.siteUser = siteUser;
        this.post = post;
    }
    public void updateUrl(String url) {
        this.url=url;
    }

    public void setPosts(Posts post) {
        this.post = post;
    }
}

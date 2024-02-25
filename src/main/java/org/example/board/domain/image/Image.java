package org.example.board.domain.image;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.user.SiteUser;
import org.springframework.security.core.parameters.P;

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
}

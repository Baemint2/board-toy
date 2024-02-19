package org.example.board.domain.image;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.domain.user.SiteUser;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_user_ID")
    private SiteUser siteUser;

    @Builder
    public Image(String url, SiteUser siteUser) {
        this.url = url;
        this.siteUser = siteUser;
    }

    public void updateUrl(String url) {
        this.url=url;
    }
}

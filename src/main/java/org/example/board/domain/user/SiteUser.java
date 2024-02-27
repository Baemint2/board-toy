package org.example.board.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.board.common.BaseTimeEntity;
import org.example.board.domain.answer.Answer;
import org.example.board.domain.image.Image;
import org.example.board.domain.postslike.PostsLike;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class SiteUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column
    @Enumerated
    private Role role;

//    @OneToMany(mappedBy = "site_user", cascade = CascadeType.REMOVE)
//    private List<Answer> answerList;

    @OneToOne(mappedBy = "siteUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    @OneToMany(mappedBy = "siteUser",cascade = CascadeType.ALL)
    private List<PostsLike> postsLikesUser = new ArrayList<>();

    @Builder
    public SiteUser(String username, String nickname, String password, String email, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }
}

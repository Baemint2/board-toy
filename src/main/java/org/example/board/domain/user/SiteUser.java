package org.example.board.domain.user;

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
public class SiteUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @Builder
    public SiteUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}

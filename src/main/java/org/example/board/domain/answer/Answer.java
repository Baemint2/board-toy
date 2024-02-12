package org.example.board.domain.answer;

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
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name="posts_id")
    private Posts posts;


    // 대댓글 구현
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Answer parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> children = new ArrayList<>();

    public void addChildAnswer(Answer child) {
        this.children.add(child);
    }

    protected void setParent(Answer parent) {
        this.parent=parent;
    }

    @Builder
    public Answer(String content, SiteUser siteUser, Posts posts, Answer parent) {
        this.content = content;
        this.siteUser = siteUser;
        this.posts = posts;
        this.parent = parent;
        if(parent !=null) {
            parent.addChildAnswer(this);
        }
    }


    public void update(String content) {
        this.content = content;
    }


}

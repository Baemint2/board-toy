package org.example.board.domain.posts.entity;

import lombok.Getter;

@Getter
public enum Category {
    ANNOUNCEMENT("공지"),
    ETC("기타"),
    SMALLTALK("잡담");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

}

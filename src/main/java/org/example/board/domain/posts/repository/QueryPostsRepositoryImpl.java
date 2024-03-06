package org.example.board.domain.posts.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.answer.entity.QAnswer;
import org.example.board.domain.posts.dto.PostsDetailResponseDto;
import org.example.board.domain.posts.entity.Category;
import org.example.board.domain.posts.entity.QPosts;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.board.domain.posts.entity.QPosts.posts;

@Slf4j
public class QueryPostsRepositoryImpl implements QueryPostsRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public QueryPostsRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


    @Override
    public PageImpl<PostsDetailResponseDto> searchByKeyword(String type, String keyword, Pageable pageable) {
        QPosts posts = QPosts.posts;
        QAnswer answer = QAnswer.answer;
        BooleanExpression expression = buildExpression(type, keyword, posts);

        List<Tuple> results =  jpaQueryFactory
                // 컬럼 명 명시 안하면 Null 전송됨
                .select(posts.id, posts.title, posts.content,
                        posts.author, posts.createdDate, posts.modifiedDate,
                        posts.viewCount, answer.count(), posts.likeCount, posts.category)
                .from(posts)
                .leftJoin(answer).on(answer.posts.eq(posts))
                .where(expression)
                .groupBy(posts.id)
                .orderBy(posts.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PostsDetailResponseDto> contents = results.stream().map(tuple -> {
            Long id = tuple.get(posts.id);
            String title = tuple.get(posts.title);
            String content = tuple.get(posts.content);
            String author = tuple.get(posts.author);
            LocalDateTime createdDate = tuple.get(posts.createdDate);
            LocalDateTime modifiedDate = tuple.get(posts.modifiedDate);
            Integer viewCount = Optional.ofNullable(tuple.get(posts.viewCount)).orElse(0);
            Integer answerCount = Optional.ofNullable(tuple.get((answer.count()))).map(Long::intValue).orElse(0);
            Integer likeCount = Optional.ofNullable(tuple.get(posts.likeCount)).orElse(0);
            String koreanName = Optional.ofNullable(tuple.get(posts.category)).map(Category::getDisplayName).orElse("기본");

            // 로그 찍기
            log.info("Post details - id: {}, title: {}, content: {}, author: {}, createdDate: {}, modifiedDate: {}, viewCount: {}, answerCount: {}, likeCount: {}, koreanName: {}",
                    id, title, content, author, createdDate, modifiedDate, viewCount, answerCount, likeCount, koreanName);

            return new PostsDetailResponseDto(id, title, content, author, createdDate, modifiedDate, viewCount, answerCount, likeCount, koreanName);
        }).collect(Collectors.toList());


        Long total = Optional.ofNullable(jpaQueryFactory
                .select(posts.count())
                .from(posts)
                .where(expression)
                .fetchOne()).orElse(0L);

        log.info("Conversion completed. Contents size: {}", contents.size());

        return new PageImpl<>(contents, pageable, total);

    }
        private BooleanExpression buildExpression(String type, String keyword, QPosts qPosts) {
        if("title".equals(type)) {
            return posts.title.contains(keyword);
        } else if ("author".equals(type)) {
            return posts.author.contains(keyword);
        } else if ("content".equals(type)) {
            return posts.content.contains(keyword);
        }

        return null;
    }
}

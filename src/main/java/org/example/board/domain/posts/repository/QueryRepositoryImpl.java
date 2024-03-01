package org.example.board.domain.posts.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.board.domain.answer.QAnswer;
import org.example.board.domain.posts.QPosts;
import org.example.board.domain.posts.dto.PostsDetailResponseDto;
import org.example.board.domain.posts.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class QueryRepositoryImpl implements QueryRepository {

    private final JPAQueryFactory queryFactory;

    public QueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    //최신 순
    @Override
    public Page<PostsDetailResponseDto> findAllByLatest(Pageable pageable) {
        return findPostsSorted(pageable, QPosts.posts.createdDate.desc());
    }

    //조회수 순
    @Override
    public Page<PostsDetailResponseDto> findAllOrderByViewCountDesc(Pageable pageable) {
        return findPostsSorted(pageable, QPosts.posts.viewCount.desc());
    }

    //댓글 많은 순
    @Override
    public Page<PostsDetailResponseDto> findAllOrderByAnswerCountDesc (Pageable pageable){
        return findPostsSorted(pageable, QAnswer.answer.count().desc());
    }

    //좋아요 순
    @Override
    public Page<PostsDetailResponseDto> findAllOrderByLikeCountDesc(Pageable pageable) {
        return findPostsSorted(pageable, QPosts.posts.likeCount.desc());
    }

    private PageImpl<PostsDetailResponseDto> findPostsSorted (Pageable pageable, OrderSpecifier < ?>... orderByConditions){
        QPosts posts = QPosts.posts;
        QAnswer answer = QAnswer.answer;

        List<Tuple> results = queryFactory
                .select(posts.id, posts.title, posts.content,
                        posts.author, posts.createdDate, posts.modifiedDate, posts.viewCount, answer.count(), posts.likeCount, posts.category)
                .from(posts)
                .leftJoin(answer).on(answer.posts.eq(posts))
                .groupBy(posts.id)
                .orderBy(orderByConditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // tuple 결과를 Dto 리스트로 변환
        List<PostsDetailResponseDto> contents = results.stream().map(tuple -> {

            Integer viewCount = Optional.ofNullable(tuple.get(posts.viewCount)).orElse(0);
            Integer answerCount = Optional.ofNullable(tuple.get((answer.count()))).map(Long::intValue).orElse(0);// 댓글 수))
            Integer likeCount = Optional.ofNullable(tuple.get(posts.likeCount)).orElse(0);
            String koreanName = Optional.ofNullable(tuple.get(posts.category)).map(Category::getDisplayName).orElse("기본");
            return new PostsDetailResponseDto(
                    tuple.get(posts.id),
                    tuple.get(posts.title),
                    tuple.get(posts.content),
                    tuple.get(posts.author),
                    tuple.get(posts.createdDate),
                    tuple.get(posts.modifiedDate),
                    viewCount,
                    answerCount,
                    likeCount,
                    koreanName);
        }).collect(Collectors.toList());

        Long total = Optional.ofNullable(queryFactory
                .select(posts.count())
                .from(posts)
                .fetchOne()).orElse(0L);
            return new PageImpl<>(contents, pageable, total);
        }

    }


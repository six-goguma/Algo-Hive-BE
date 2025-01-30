package com.knu.algo_hive.post.repository;

import com.knu.algo_hive.post.dto.PostSummaryResponse;
import com.knu.algo_hive.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {


    @Query("SELECT new com.knu.algo_hive.post.dto.PostSummaryResponse(p.id,p.title,p.thumbnail,p.summary,p.createdAt,p.likeCount,p.commentCount,m.nickName) " +
            "FROM Post p " +
            "JOIN p.member m ")
    Page<PostSummaryResponse> findPostSummariesPaged(Pageable pageable);

    @Query("SELECT new com.knu.algo_hive.post.dto.PostSummaryResponse(p.id, p.title, p.thumbnail, p.summary, p.createdAt, p.likeCount, p.commentCount, m.nickName) " +
            "FROM Post p " +
            "JOIN p.member m " +
            "WHERE m.nickName = :nickname")
    Page<PostSummaryResponse> findPostSummariesByAuthorPaged(Pageable pageable, @Param("nickname") String nickname);

    @Query("SELECT p " +
            "FROM Post p " +
            "JOIN FETCH p.member m " +
            "WHERE p.id = :postId  ")
    Optional<Post> findByPostId(@Param("postId") Long postId);

    @Query("SELECT new com.knu.algo_hive.post.dto.PostSummaryResponse(pt.post.id, pt.post.title, pt.post.thumbnail, pt.post.summary, pt.post.createdAt, pt.post.likeCount, pt.post.commentCount, m.nickName) " +
            "FROM PostTag pt " +
            "JOIN pt.post p " +
            "JOIN p.member m " +
            "WHERE pt.tagId = :tagId ")
    Page<PostSummaryResponse> findPostSummariesBtTagIdPaged(@Param("tagId") int tagId, Pageable pageable);

    @Query("SELECT new com.knu.algo_hive.post.dto.PostSummaryResponse(pt.post.id, pt.post.title, pt.post.thumbnail, pt.post.summary, pt.post.createdAt, pt.post.likeCount, pt.post.commentCount, m.nickName) " +
            "FROM PostTag pt " +
            "JOIN pt.post p " +
            "JOIN p.member m " +
            "WHERE pt.tagId = :tagId AND m.nickName = :nickName ")
    Page<PostSummaryResponse> findPostSummariesBtTagIdAndNickname(@Param("tagId") int tagId, @Param("nickname") String nickname, Pageable pageable);
}

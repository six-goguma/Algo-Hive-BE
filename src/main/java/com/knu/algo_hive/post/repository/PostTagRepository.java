package com.knu.algo_hive.post.repository;

import com.knu.algo_hive.post.entity.Post;
import com.knu.algo_hive.post.entity.PostTag;
import com.knu.algo_hive.post.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
    @Query("SELECT pt.tagId  " +
            "FROM PostTag pt " +
            "WHERE pt.post.id = :postId ")
    List<Integer> findAllByPostId(@Param("postId") Long postId);

    void deleteAllByPost(Post post);
}

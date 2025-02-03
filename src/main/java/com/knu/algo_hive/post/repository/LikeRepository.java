package com.knu.algo_hive.post.repository;

import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.post.entity.Like;
import com.knu.algo_hive.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    int countByPostId(Long postId);

    Optional<Like> findByPostAndMember(Post post, Member member);

    boolean existsByPostIdAndMemberEmail(Long postId, String email);
}

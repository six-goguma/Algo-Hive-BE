package com.knu.algo_hive.post.repository;

import com.knu.algo_hive.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

package com.knu.algo_hive.post.service;

import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.auth.repository.MemberRepository;
import com.knu.algo_hive.common.exception.ForbiddenException;
import com.knu.algo_hive.common.exception.NotFoundException;
import com.knu.algo_hive.post.dto.CommentRequest;
import com.knu.algo_hive.post.dto.CommentResponse;
import com.knu.algo_hive.post.entity.Comment;
import com.knu.algo_hive.post.entity.Post;
import com.knu.algo_hive.post.repository.CommentRepository;
import com.knu.algo_hive.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(Long postId, Pageable pageable) {
        return commentRepository.findCommentsByPostIdPaged(postId, pageable);
    }

    @Transactional
    public void saveComment(CommentRequest request, Long postId,String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new NotFoundException("post not found"));
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("member not found"));

        commentRepository.save(new Comment(request.content(), member, post));
    }

    @Transactional
    public void updateComment(CommentRequest request, Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new NotFoundException("comment not found"));
        if(!comment.getMember().getEmail().equals(email)){
            throw new ForbiddenException("이 댓글은 당신의 것이 아닙니다");
        }

        comment.updateContent(request.content());
    }

    @Transactional
    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new NotFoundException("comment not found"));
        if(!comment.getMember().getEmail().equals(email)){
            throw new ForbiddenException("이 댓글은 당신의 것이 아닙니다");
        }

        commentRepository.delete(comment);
    }
}

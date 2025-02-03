package com.knu.algo_hive.post.service;

import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.auth.repository.MemberRepository;
import com.knu.algo_hive.common.exception.NotFoundException;
import com.knu.algo_hive.post.dto.LikeCountResponse;
import com.knu.algo_hive.post.dto.LikeStatusResponse;
import com.knu.algo_hive.post.entity.Like;
import com.knu.algo_hive.post.entity.Post;
import com.knu.algo_hive.post.repository.LikeRepository;
import com.knu.algo_hive.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, MemberRepository memberRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public LikeCountResponse getLikeCount(Long postId) {
        return new LikeCountResponse(likeRepository.countByPostId(postId));
    }

    @Transactional
    public LikeStatusResponse changeLikeStatus(Long postId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("member not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("post not found"));

        Optional<Like> likeOptional = likeRepository.findByPostAndMember(post, member);
        if (likeOptional.isPresent()) {
            likeOptional.ifPresent(likeRepository::delete);
            post.setLikeCount(post.getLikeCount() - 1);
            return new LikeStatusResponse(false);
        } else {
            likeRepository.save(new Like(member, post));
            post.setLikeCount(post.getLikeCount() + 1);
            return new LikeStatusResponse(true);
        }
    }

    @Transactional(readOnly = true)
    public LikeStatusResponse getLikeStatus(Long postId, String email) {
        if (likeRepository.existsByPostIdAndMemberEmail(postId, email)) {
            return new LikeStatusResponse(true);
        } else {
            return new LikeStatusResponse(false);
        }
    }
}

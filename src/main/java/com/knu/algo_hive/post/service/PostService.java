package com.knu.algo_hive.post.service;

import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.auth.repository.MemberRepository;
import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.common.exception.ForbiddenException;
import com.knu.algo_hive.common.exception.NotFoundException;
import com.knu.algo_hive.post.dto.PostRequest;
import com.knu.algo_hive.post.dto.PostResponse;
import com.knu.algo_hive.post.dto.PostSummaryResponse;
import com.knu.algo_hive.post.entity.Post;
import com.knu.algo_hive.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getPostSummaries(Pageable pageable) {
        return postRepository.findPostSummariesPaged(pageable);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getPostSummariesByNickname(Pageable pageable, String nickname) {
        return postRepository.findPostSummariesByAuthorPaged(pageable, nickname);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(),
                post.getThumbnail(), post.getSummary(), post.getLikeCount(), post.getCommentCount(),
                post.getCreatedAt(), post.getUpdatedAt(), post.getMember().getNickName());
    }

    @Transactional
    public void savePost(PostRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        postRepository.save(new Post(request.contents(), request.summary(), request.thumbnail(), request.title(), member));
    }

    @Transactional
    public void updatePost(Long postId, PostRequest request, String email) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getEmail().equals(email)) throw new ForbiddenException(ErrorCode.NOT_YOUR_RESOURCE);

        post.setTitle(request.title());
        post.setContent(request.contents());
        post.setThumbnail(request.thumbnail());
        post.setSummary(request.summary());
    }

    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getEmail().equals(email)) throw new ForbiddenException(ErrorCode.NOT_YOUR_RESOURCE);

        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getAllPostSummariesByTag(int tagId, Pageable pageable) {
        return postRepository.findPostSummariesByTagIdPaged(tagId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getPostSummariesByTagIdAndNickname(int tagId, String nickname, Pageable pageable) {
        return postRepository.findPostSummariesBtTagIdAndNickname(tagId, nickname, pageable);
    }
}

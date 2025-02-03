package com.knu.algo_hive.post.service;

import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.common.exception.ForbiddenException;
import com.knu.algo_hive.common.exception.NotFoundException;
import com.knu.algo_hive.post.dto.TagRequest;
import com.knu.algo_hive.post.dto.TagResponse;
import com.knu.algo_hive.post.entity.Post;
import com.knu.algo_hive.post.entity.PostTag;
import com.knu.algo_hive.post.repository.PostRepository;
import com.knu.algo_hive.post.repository.PostTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagService {

    private final PostTagRepository postTagRepository;
    private final PostRepository postRepository;

    public TagService(PostTagRepository postTagRepository, PostRepository postRepository) {
        this.postTagRepository = postTagRepository;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public TagResponse getTags(Long postId) {
        return new TagResponse(postTagRepository.findAllByPostId(postId));
    }

    @Transactional
    public void saveOrUpdateTags(Long postId, TagRequest request, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        if (!post.getMember().getEmail().equals(email)) throw new ForbiddenException(ErrorCode.NOT_YOUR_RESOURCE);

        postTagRepository.deleteAllByPost(post);

        if (!request.tagIds().isEmpty()) {
            Set<PostTag> newPostTags = new HashSet<>();
            for (int tagId : request.tagIds()) {
                newPostTags.add(new PostTag(post, tagId));
            }
            postTagRepository.saveAll(newPostTags);
        }
    }
}

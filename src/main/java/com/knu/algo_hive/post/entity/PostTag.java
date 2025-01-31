package com.knu.algo_hive.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(PostTagId.class)
@Getter
@NoArgsConstructor
public class PostTag {
    @Id
    private int tagId;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public PostTag(Post post, int tagId) {
        this.post = post;
        this.tagId = tagId;
    }
}

package com.knu.algo_hive.post.entity;

import java.io.Serializable;
import java.util.Objects;

public class PostTagId implements Serializable {
    private int tagId;
    private Long post;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        PostTagId that = (PostTagId) obj;
        return tagId == that.tagId && post.equals(that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, post);
    }
}

package com.knu.algo_hive.post.entity;

import com.knu.algo_hive.auth.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String thumbnail;
    private String summary;
    private int likeCount = 0;
    private int commentCount = 0;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Post(String content, String summary, String thumbnail, String title, Member member) {
        this.content = content;
        this.summary = summary;
        this.thumbnail = thumbnail;
        this.title = title;
        this.member = member;
    }
}

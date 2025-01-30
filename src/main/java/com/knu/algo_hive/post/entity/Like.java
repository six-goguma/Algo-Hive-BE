package com.knu.algo_hive.post.entity;

import com.knu.algo_hive.auth.entity.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public Like(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}

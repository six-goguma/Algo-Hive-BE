package com.knu.algo_hive.ai.entity;

import com.knu.algo_hive.auth.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
public class GeminiRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 3000)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    protected GeminiRequestEntity() {

    }

    public GeminiRequestEntity(String code, Member member) {
        this.code = code;
        this.member = member;
    }
}

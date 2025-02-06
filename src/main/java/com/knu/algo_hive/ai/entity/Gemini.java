package com.knu.algo_hive.ai.entity;

import com.knu.algo_hive.auth.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Gemini {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10000)
    private String response;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    @Column(length = 3000)
    private String code;
    @CreatedDate
    @NotNull
    private LocalDateTime createdDateTime;

    protected Gemini() {

    }

    public Gemini(String response, String code, Member member) {
        this.response = response;
        this.code = code;
        this.member = member;
    }

    public boolean checkMemberIsNot(Member member) {
        return this.member != member;
    }
}

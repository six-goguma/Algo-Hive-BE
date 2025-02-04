package com.knu.algo_hive.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Profile(String url, Member member){
        this.url = url;
        this.member = member;
    }

    public Profile(Member member){
        this.url = null;
        this.member = member;
    }

    public void updateUrl(String url){
        this.url = url;
    }
}

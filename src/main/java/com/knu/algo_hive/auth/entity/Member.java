package com.knu.algo_hive.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Entity
@Getter

public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true)
    private String nickName;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    public Member() {
    }

    public Member(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public void putNickName(String nickName){
        this.nickName = nickName;
    }
}

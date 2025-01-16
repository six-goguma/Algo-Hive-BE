package com.knu.algo_hive.ai.entity;

import jakarta.persistence.*;

@Entity
public class GeminiResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10000)
    private String response;

    @OneToOne
    @JoinColumn(name = "request_id")
    private GeminiRequestEntity geminiRequestEntity;

    protected GeminiResponseEntity() {

    }

    public GeminiResponseEntity(String response, GeminiRequestEntity geminiRequestEntity) {
        this.response = response;
        this.geminiRequestEntity = geminiRequestEntity;
    }
}

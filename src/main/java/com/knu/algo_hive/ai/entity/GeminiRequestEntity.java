package com.knu.algo_hive.ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class GeminiRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 3000)
    private String code;
    @CreatedDate
    @NotNull
    private LocalDateTime createdDateTime;

    protected GeminiRequestEntity() {

    }

    public GeminiRequestEntity(String code) {
        this.code = code;
    }
}

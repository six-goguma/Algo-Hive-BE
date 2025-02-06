package com.knu.algo_hive.ai.repository;

import com.knu.algo_hive.ai.entity.GeminiResponseEntity;
import com.knu.algo_hive.auth.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeminiResponseRepository extends JpaRepository<GeminiResponseEntity, Long> {
    Page<GeminiResponseEntity> findAllByMember(Member member, Pageable pageable);
}

package com.knu.algo_hive.ai.repository;

import com.knu.algo_hive.ai.entity.GeminiRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeminiRequestRepository extends JpaRepository<GeminiRequestEntity, Long> {
}

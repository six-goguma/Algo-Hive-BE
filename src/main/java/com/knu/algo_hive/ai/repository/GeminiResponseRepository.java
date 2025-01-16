package com.knu.algo_hive.ai.repository;

import com.knu.algo_hive.ai.entity.GeminiResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeminiResponseRepository extends JpaRepository<GeminiResponseEntity, Long> {
}

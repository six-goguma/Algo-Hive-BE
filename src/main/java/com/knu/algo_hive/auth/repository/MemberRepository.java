package com.knu.algo_hive.auth.repository;

import com.knu.algo_hive.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    Optional<Member> findByEmail(String email);
}

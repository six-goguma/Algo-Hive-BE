package com.knu.algo_hive.auth.repository;

import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.auth.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByMember(Member member);
}

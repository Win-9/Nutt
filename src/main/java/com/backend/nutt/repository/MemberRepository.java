package com.backend.nutt.repository;

import com.backend.nutt.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(String Id);

    Optional<Member> findByEmail(String email);
}

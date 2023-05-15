package com.backend.nutt.repository;

import com.backend.nutt.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(String Id);

    @EntityGraph(attributePaths = "dailyIntakeList")
    Optional<Member> findByEmail(String email);

    boolean existsMemberByEmail(String email);
}

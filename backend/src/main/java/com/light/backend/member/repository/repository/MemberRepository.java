package com.light.backend.member.repository.repository;

import com.light.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsById(String id);

    Optional<Member> findById(String id);

}

package org.com.jwtshop.domain.member.repository;

import org.com.jwtshop.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndIsDeletedFalse(Long member_id); //쿼리 메서드
    boolean existsMemberByName(String name);
}

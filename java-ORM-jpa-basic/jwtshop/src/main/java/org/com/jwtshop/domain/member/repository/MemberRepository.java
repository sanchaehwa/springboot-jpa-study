package org.com.jwtshop.domain.member.repository;

import org.com.jwtshop.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMemberByName(String name);
}

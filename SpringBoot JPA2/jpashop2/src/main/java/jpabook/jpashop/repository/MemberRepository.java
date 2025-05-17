package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> { //<객체 , PK Type>
    //select m from Member m where m.name = ?
    List<Member> findByName(String name);
}

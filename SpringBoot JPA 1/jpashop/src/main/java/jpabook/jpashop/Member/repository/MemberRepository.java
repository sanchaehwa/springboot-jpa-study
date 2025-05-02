package jpabook.jpashop.Member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.Member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {
    //영속성 메니저
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member); //Side Effect : 외부 상태를 변경하는것, DB 상태룰 변경 => Command
        return member.getId(); //command 와 Query 를 분리 원칙 : 명령과 조회 분리 (그래서 최소한으로 ID값으로 Member를 조회할 수 있도록,
    }
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
    public List<Member> findAll() {
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
        return result;
    }
//    public List<Member> findByName(String name) {
//        return em.createQ
//    }

}

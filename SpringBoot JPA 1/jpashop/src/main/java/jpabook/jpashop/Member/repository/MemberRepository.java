package jpabook.jpashop.Member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.Member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository{
    //엔티티메니저를 직접 써서 구현한 JPA 방식 (강의 방식)

     //영속성 메니저 @PersistenceContext
    private final EntityManager em;

    //Member 저장
    public Long save(Member member) {
        em.persist(member); //Side Effect : 외부 상태를 변경하는것, DB 상태룰 변경 => Command
        return member.getId(); //command 와 Query 를 분리 원칙 : 명령과 조회 분리 (그래서 최소한으로 ID값으로 Member를 조회할 수 있도록,
    }

    //ID 값으로 Member 조회
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
    //Member 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();

    }
    //이름으로 Member 조회
    public List<Member> findByName(String username) {
        return em.createQuery("select m from Member m where m.username= :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

}

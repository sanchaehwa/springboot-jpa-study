package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 상태
/* 데이터 저장
        //비영속
        Member member = new Member();
        member.setId(1L);
        member.setName("Jack");

        //엔티티 메니저를 사용해서 회원 엔티티를 영속성 컨텍스트에 저장한다.
        em.persist(member);

        //DB에 저장
        tx.commit();

        em.close(); //영속성 컨텍스트에서 관리되던 엔티티는 모두 준영속

           try {
            Member member = new Member();
            member.setId(2L);
            member.setName("John");

            em.persist(member);
            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
 */ //데이터 변경

        try {
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("Apple");
            //Member 객체를 대상으로 Query : 객체 지향 쿼리
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    //가져올 부분을 설정해주는 페이징 네이션 (setFirst ~ setMax)
//                    .setFirstResult(0)
//                    .setMaxResults(8)
//                    .getResultList();
//            for (Member member : result) {
//                System.out.println(member.getName());
            Member member = new Member();
//            //Member 객체 1 생성
//            member.setId(1L);
//            member.setUsername("A");
//            member.setRoleType(RoleType.USER);
//            //Member 객체 2 생성
//            member.setId(2L);
//            member.setUsername("B");
//            member.setRoleType(RoleType.ADMIN);
           // Member 객체 3 생성
            //  member.setId(3L);
            member.setUsername("C");
            //member.setRoleType(RoleType.GUEST);

            em.persist(member);

            tx.commit();
        }
        catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    }


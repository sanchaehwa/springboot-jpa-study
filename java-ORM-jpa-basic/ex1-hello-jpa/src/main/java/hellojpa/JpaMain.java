package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 상태
        try {
            //데이터 삽입
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("Sam");
            member.changeTeam(team);
            em.persist(member); //member 엔티티를 영속성 컨텍스트에 등록

            team.addMember(member);

            em.flush();
            em.clear();
            /**
             -영속성 컨택스트가 아닌 DB에서 조회하게 만들려면 em 초기화

            데이터 조회 (객체 지향적이지 않는 방법)
            Member findMember = em.find(Member.class, member.getId());
            Long findTeamId = findMember.getTeamId();
            Team findTeam = em.find(Team.class, findTeamId);
            **/
           // Member findMember = em.find(Member.class, member.getId());//JPA는 DB에 조회 쿼리 안 날리고 1차캐시에서 바로 꺼내옴.
            Team findTeam = em.find(Team.class, team.getId());
            List<Member>members = findTeam.getMembers();
            //Team findTeam = findMember.getTeam();
            System.out.println(" ------");
            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println(" ------");

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


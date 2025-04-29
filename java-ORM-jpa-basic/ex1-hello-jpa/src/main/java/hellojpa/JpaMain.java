package hellojpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 상태
        try {
            //팀객체 설정
            Team team1 = new Team();
            team1.setName("Team 1");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("Team 2");
            em.persist(team2);

            Member member1 = new Member();
            member1.setUsername("joy");
            member1.setTeam(team1);

            //시간 객체 설정
            Period period1 = new Period();
            period1.setStartDate(LocalDateTime.now());
            period1.setEndDate(LocalDateTime.now().plusDays(1));
            member1.setPeriod(period1);

            //주소 객체 설정
//            Address address1 = new Address();
//            address1.setStreet("Haeundae");
//            address1.setCity("Busan");
//            address1.setZipcode("23-1");
           // member1.setAddress(new Address("Haeundae","Busan","23-1"));

            Address address = new Address("Haeundae","Busan","23-1");
            member1.setAddress(address);
            member1.setPeriod(new Period(LocalDateTime.now(),LocalDateTime.now().plusDays(1)));

            //List 와 Hash 는 값을 추가하는 개념 (컬렉션 타입)
            member1.getFavoriteFoods().add("chicken");
            member1.getFavoriteFoods().add("pasta");
            member1.getFavoriteFoods().add("pizza");

            member1.getAddressHistory().add(new AddressEntity("Haeundae", "Busan", "23-1"));
            member1.getAddressHistory().add(new AddressEntity("Haeundae","Busan","23-1"));
            member1.getAddressHistory().add(new AddressEntity("Gwangalli","Busan","24-1"));


            em.persist(member1);

            Member member2 = new Member();
           // Address address2 = new Address("Gwangalli","Busan","24-1");
            Address copyaddress = new Address(address.getStreet(), address.getCity(), address.getZipcode());
            member2.setUsername("sunny");
            member2.setTeam(team2);
            member2.setAddress(copyaddress);
            member2.setPeriod(new Period(LocalDateTime.now(),LocalDateTime.now().plusDays(1)));
            em.persist(member2);

            //같은 Address 사용후 값 변경 -같은 값타입을 사용하고 하나의 객체의 값타입을 변경하더라도 이 값타입을 사용하고있는 member2도바뀜 => 부작용
            //복사해서 사용 (copyaddress)
            member1.getAddress().setStreet("Seo-myeon");

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.find(Member.class, member2.getId());
            System.out.println("m2.getClass() == m1.getClass9 = " + (m2.getClass() == m1.getClass())); //true

            logic(m1,m2);
            
            Member reference = em.getReference(Member.class, member1.getId());
            System.out.println("reference.getClass() = " + reference.getClass());

            System.out.println("a = a "+ (m1 == reference));


            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember.getClass() = " + refMember.getClass());

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember.getClass() = " + findMember.getClass());
            System.out.println("m.getTeam() = " + m1.getTeam().getClass());

            System.out.println("a==a" +  ( refMember == findMember)); //true

            List<Member>members = em.createQuery("select m from Member m",Member.class).getResultList();
            //SQL : select * from Member  : 100명
            //SQL : select * from Team where TEAM_ID **; => N+1 문제 발생

            //영속성 전이 - child 객체도 parent 객체도 영속상태로 만들어소 함께 저장시킬때
            Child child1 = new Child();
            child1.setName("Child 1");

            Child child2 = new Child();
            child2.setName("Child 2");

            Parent parent1 = new Parent();
            parent1.setName("Parent 1");
            parent1.addChild(child1);
            parent1.addChild(child2);
            em.persist(parent1);



            Parent parent = em.find(Parent.class, parent1.getId());
            Child childToRemove = parent.getChildList().get(0);  // 첫 번째 Child 객체를 가져와서 제거

            parent.removeChild(childToRemove);
            //제거한 변경사항을 DB에 반영
            em.flush();
            //영속성 컨텍스트 비움
            em.clear();

            Parent parentAfter = em.find(Parent.class, parent1.getId()); //부모 객체에서 자식들을 조회하면, 삭제된 Child 는 포함되지 않음.
            List<Child> remainingChildren = parentAfter.getChildList();

            System.out.println("remainingChildren = " + remainingChildren); //child1 은 삭제 child2 는 남아있음.

            tx.commit();
        }


        catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    private static void printMember(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);
        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }
    private static void logic(Member m1, Member m2) {
        System.out.println("m1 instanceof  Member = " + (m1 instanceof  Member)); //어떤 클래스를 상속받았는지 확인 - 실제 타입(Member 클래스 상속)
        System.out.println("m2 instanceof  Member = " + (m2 instanceof  Member));
    }


}


package hellojpa.jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //code
        try {
            Team team = new Team();
            team.setTeamname("Team 1");
            em.persist(team);

            Member member = new Member();
            member.setUsername("sam");
            member.setAge(5);
            member.setType(MemberType.ADMIN);
            member.setTeam(team);
            em.persist(member);


            /** TypedQuery , Query **/

            //Member.class 반환 객체 명확
            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class); // Entity Projection
            List<Member> resultList = query.getResultList();
            //DB에 값이 하나만 있는 경우(정확히 하나만 있는경우)
            //Member result = query.getSingleResult();
            //System.out.println("result = " + result);
            //파라미터 바인딩(이름 기준) - 위치가 꼬여도 버그 발생 안함
            TypedQuery<String> query1 = em.createQuery("select m.username from Member m where m.username = :username", String.class);
            query1.setParameter("username", "sam"); //sam 이름을 갖고있는 Member 조회

            //매서드 체이닝 : 매서드를 체인으로 엮듯이 호출
//            Member result1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
//                    .setParameter("username", "sam")
//                    .getSingleResult();
//            System.out.println("result1 = " +result1.getUsername());

            List<String> resultList1 = query1.getResultList(); //정확히 하나의 결과만 기대한다 하면 getSingleResult
            System.out.println("resultList1 = " + resultList1);

            //Entity Projection
            List<Team> resultList2 = em.createQuery("SELECT t FROM Member m join m.team t  ", Team.class).getResultList(); //명시적 조인이 좋음

            //Embedded Projection
            List<Address> resultList3 = em.createQuery("select o.address from Order o", Address.class).getResultList();

            //Scalar Projection - 여러개의 필드를 Scalar로 조회하면 한줄의 결과 Object[]
            List<MemberDTO> resultList4 = em.createQuery(
                    "select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class
            ).getResultList();

            for (MemberDTO dto : resultList4) {
                System.out.println("username = " + dto.getUsername());
                System.out.println("age = " + dto.getAge());
            }            //Scalar Projection 조회 - 타입을 지정을 못해서 Object 가 들어감 이 Object 형변환



            //복합체 membername은 String인데, age는 Integer 타입
            Query query3 = em.createQuery("select m.username, m.age FROM Member m");

            //페이징 API(20개의 데이터를 생성) - 페이징: 많은 데이터를 처리
//            for (int i = 0; i < 20; i++){
//                Team team = new Team();
//                team.setTeamname("team" + i);
//                em.persist(team);
//
//                Member member = new Member();
//                member.setUsername("sam" + i);
//                member.setAge(i);
//                member.setTeam(team);
//                em.persist(member);
//
//            }
            em.flush();
            em.clear();


           /** Join / 조건식 CASE 문 **/

            String query6 = "select m from Member m inner join m.team t";
            List<Member> resultList6 = em.createQuery(query6, Member.class).getResultList();
            System.out.println("resultList6 = " + resultList6);

            List<Member> resultList5 = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("resulList.size = " + resultList5.size());
            for (Member member1 : resultList5) {
                System.out.println("username = " + member);
            }

            String query7 = "SELECT m.username, 'HELLO', true " +
                    "FROM Member m " +
                    "WHERE m.type = :userType";

            List<Object[]> resultList8 = em.createQuery(query7).setParameter("userType",MemberType.ADMIN).getResultList();

            resultList8.forEach(row -> {
                String username = (String) row[0];   // sam
                String str = (String) row[1];        // "HELLO"
                Boolean bool = (Boolean) row[2];     // true

                System.out.println("Member: " + username + ", Text: " + str + ", Flag: " + bool);
            });

            List<Object[]> resultList7 = em.createQuery(" SELECT m, t FROM Member m LEFT JOIN m.team t on t.teamname = 'A'").getResultList();
            //형변환
           resultList7.forEach(row -> {
               Member m = (Member) row[0];
               Team t = (Team) row[1];
               System.out.println( "Member= " + m + "Team = " + t);

           });
            String query8 = "SELECT " +
                    "CASE " +
                    "   WHEN m.age <= 10 THEN 'student_fee' " +
                    "   WHEN m.age >= 60 THEN 'elders_fee' " +
                    "   ELSE 'general_fee' " +
                    "END " +
                    "FROM Member m";
           List<String>resultList9 = em.createQuery(query8,String.class).getResultList();
           for(String s : resultList9){
               System.out.println(s);
           }
           /** 사용자 정의 함수 (기본함수) **/

            //Orcal 데이터 베이스에서 사용되는 특수한 더미 테이블. 테이블 없이 Select 하려면 DUAL 사용 - JPQL은 DUAL 개념이 없어서, 항상 엔티티 기준
            //Hibernate의 경우 Concat(문자열 덧셈을 수행하는 기본함수) 과 동일 연산자 || 제공  SELECT 'a' || 'b' FROM Player p;
            String query9 = "SELECT CONCAT('a','b') FROM Member m";
            List<String> resultList10 = em.createQuery(query9, String.class).getResultList();
            for (String s : resultList10) {
                System.out.println(s);
            }
            //SUBSTRING : 문자열 일부를 잘라냄 (2 - 4까지 잘라낸다는것 (2,3)
            String query10 = "SELECT SUBSTRING('Hello' , 2, 3) FROM Member m";
            List<String> resultList11 = em.createQuery(query10, String.class).getResultList();
            for (String s : resultList11) {
                System.out.println(s);
            }
            //Lower, Upper 소문자 - 대문자
            String query11 ="SELECT LOWER('Java') , UPPER('PYthon') FROM Member m";
            List<String> resultList12 = em.createQuery(query11, String.class).getResultList();
            for (String s : resultList12) {
                System.out.println(s);
            }
            //Locate: 부분 문자열의 시작 위치 반환 (없으면 0)
            String query12 = "SELECT LOCATE('el', 'hello') FROM Member m";//2
            List<String> resultList13 = em.createQuery(query12, String.class).getResultList();
            for (String s : resultList13) {
                System.out.println(s);
            }
            /** 경로 함수 **/
            //


            tx.commit();

        }catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }

        emf.close();
    }
}

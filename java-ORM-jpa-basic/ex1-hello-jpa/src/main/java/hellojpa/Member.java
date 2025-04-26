package hellojpa;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name="Member")
@Getter
@Setter
public class Member extends BaseEntity{


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "MEMBER_ID", nullable = false)
        private Long id;

        @Column(name = "USERNAME",nullable = false)
        private String username;
        //지연로딩 : 연관객체를 사용할때 로딩하는것 (기본값) Eager: 즉시 로딩 연관객체를 함께 즉시 로딩)

        @ManyToOne(fetch = FetchType.LAZY) //지연로딩
        @JoinColumn(name="TEAM_ID") //일대다 관계 : Entity 의 연관관계에서 외래 키를 매핑하기위해 사용(Member 테이블에 Team_id 값을 매핑하기 위해 )
        private Team team;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="LOCKER_ID")
        private Locker locker;

        @ManyToMany //다대다 (실무에서는 잘 안쓰임)
        @JoinTable(name="MEMBER_PRODUCT")
        private List<Product> products = new ArrayList<>();

        @OneToMany(mappedBy = "member")
        private List<MemberProduct> memberProducts = new ArrayList<>();



        public Member() {
        }
        //편의매서드
        public void changeTeam(Team team) {
                this.team = team;
                team.getMembers().add(this);
        }
        //편의 매서드
        public void assignLocker(Locker locker) {
                this.locker = locker;
                locker.setMember(this);
        }



}




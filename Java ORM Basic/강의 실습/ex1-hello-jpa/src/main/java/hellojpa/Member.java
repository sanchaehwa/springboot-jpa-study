package hellojpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="Member")
@Getter
@Setter
public class Member {


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
        //기간
        @Embedded
        private Period period;
        //주소(Address 임베디드 객체_
        @Embedded //맴버 테이블에 필드로 합쳐서 (+Member 테이블에 street zipxode city - 한 명당 한 주소를 나타낼때)
        private Address address;

        //값 타입 컬렉션을 쓰는것보다 연관관계 매핑이 좋음
        @ElementCollection //값 타입 컬랙션을 다룰 떄 사용하는 어노테이션
        @CollectionTable(name = "FAVORITE_FOOD",joinColumns = @JoinColumn(name = "MEMBER_ID")) //Member ID로 Member 엔티티와 연결 Member 하나 - Food 여러개
        @Column(name = "FOODNAME") //Favorite food 테이블 - memberid (FK) , food name (음식명 데이터)
        private Set<String> favoriteFoods = new HashSet<>(); //중복 없음 Hash

//        @ElementCollection //Address 자체가 이미 객체로 되어 있으니깐 컬럼을 쓰려고 하면, 재정의가 필요함 AttributeOverrides - 만약에 @Column으로 지정안해주면 그냥 기존처럼 street zipcode city로 매핑
//        //한명당 여러 주소
//        @CollectionTable(name = "ADDRESSHISTORY",joinColumns = @JoinColumn(name = "MEMBER_ID"))
//        //@OrderColumn(name = "address_history_order") //순서를 추적하고 순서를 유지하기 위해 - 순서 번호를 별도 칼럼에 저장
//        private List<Address> addressHisotry = new ArrayList<>(); //중복 있음 list
        @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true) //부모 - 자식 연결이 끊기면 자식 엔티티 자도 ㅇ삭제
        @JoinColumn(name = "MEMBER_ID")
        private List<AddressEntity> addressHistory = new ArrayList<>();

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




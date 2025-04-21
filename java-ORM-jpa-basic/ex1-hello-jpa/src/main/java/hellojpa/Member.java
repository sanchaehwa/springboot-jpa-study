package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
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
        @JoinColumn(name="TEAM_ID")
        private Team team;



        public Member() {
        }
        //편의매서드
        public void changeTeam(Team team) {
                this.team = team;
                team.getMembers().add(this);
        }

}




package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Team")
public class Team {
    @Id
    @GeneratedValue
    @Column(name="TEAM_ID", nullable=false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    protected Team() {}


    public void addMember(Member member) {
        member.setTeam(this); //member 객체 주입 : 양방향 연관관계 주입 (DB 외래키 설정) *주인쪽
        members.add(member); //메모리 상의 컬렉션 유지 (읽기 전용)
    }
}

package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue
    @Column(name="TEAM_ID", nullable=false)
    private Long id;

    @Column(name = " NAME", nullable = false)
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    protected Team() {}


    public void addMember(Member member) {
        member.setTeam(this);
        members.add(member);
    }
}

package hellojpa.jpql;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor

@Getter
@Setter
@Table(name="members")
//NamedQuery : 애플리케이션 로딩 시점에 쿼리 검증
@NamedQuery(name = "Member.findByUsername",query="select m from Member m where m.username = :username" )

public class Member {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Integer age;

    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", team=" + (team != null ? team.getTeamname() : "null") +
                '}';
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }




}

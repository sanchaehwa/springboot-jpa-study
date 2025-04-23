package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Locker")
@Getter
@Setter
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LOCKER_ID", nullable=false)
    private Long id;

    @Column(name ="NAME", nullable = false)
    private String name;

    @OneToOne(mappedBy ="locker")
    private Member member; //Member 객체 하나를 참조하는 관계


    //기본생성자
    protected Locker() { }

}

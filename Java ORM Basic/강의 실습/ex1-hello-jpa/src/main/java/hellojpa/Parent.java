package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="parent")
@Getter
@Setter
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //child 객체 가지고 옴
    @OneToMany(mappedBy="parent", cascade=CascadeType.ALL, orphanRemoval=true)
    //OrphaRemoval
    private List<Child> childList = new ArrayList<>();


    //편의매서드
    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }
    public void removeChild(Child child) {
        childList.remove(child);  // 컬렉션에서 해당 Child를 제거 -> Child객체는 고아상태 orphanRemoval true로설정해놨으니깐 자동으로 Child 삭제
        child.setParent(null);     // 연관관계 제거
    }
}

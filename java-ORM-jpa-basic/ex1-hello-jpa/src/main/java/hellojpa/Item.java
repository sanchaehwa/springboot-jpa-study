package hellojpa;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="item")
@Inheritance(strategy=InheritanceType.JOINED) //단일테이블 = Single_table
@DiscriminatorColumn(name="dtype") //하위 테이블 구분 컬럼 생성 -> 자식 클래스에서 @Column 필드를 사용하지않다는것
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    protected String name;
    protected String price;

}

package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Product")

public class Product {
    @Id //PK 설정
    @GeneratedValue
    @Column(name="PRODUCT_ID", nullable = false)
    private Long id;

    @Column(name="PRODUCT_NAME", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "products")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy="product")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    //기본생성자
    protected Product() {}



}

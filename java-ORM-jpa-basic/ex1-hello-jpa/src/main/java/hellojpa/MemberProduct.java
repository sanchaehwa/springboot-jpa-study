package hellojpa;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="MemberProduct")
@Getter@Setter
public class MemberProduct {
    @Id
    @GeneratedValue
    @Column(name="MemberProduct_ID",nullable =false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="PRODUCT_ID")
    private Product product;
}

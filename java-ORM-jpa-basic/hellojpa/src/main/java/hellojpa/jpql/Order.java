package hellojpa.jpql;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name ="orders")
@NoArgsConstructor
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(nullable = false)
    private Integer orderAmount;

    @Embedded
    private Address address;
}

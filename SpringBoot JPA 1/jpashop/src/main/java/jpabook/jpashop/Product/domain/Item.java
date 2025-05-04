package jpabook.jpashop.Product.domain;
//슈퍼타입(Item) - 서브타입(Book , Movie, Album) 부모 - 공통속성
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jpabook.jpashop.Product.dto.UpdateBookRequest;
import jpabook.jpashop.Product.exception.NotEnoughStockException;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 슈퍼타입 - 서브타입으로 나눠져 있긴하지만 DB에는 ITEM 테이블에 Movie , Book ,Album이 통합해서 들어감
@DiscriminatorColumn(name = "dtype")
@SuperBuilder //상속관계에서는 상속받은 필드도 Builder에서 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="item")
public  abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Min(0)
   // private int price; //int는 null을 허용하지않기때문에 아무것도 입력하지않으면 0으로 채움
    private Integer price; //Integer는 null 허용, null 바인딩 - Controller에서 @Valid + BindingResult로 검증할수 있음

    @NotNull(message ="수량을 입력해주세요")
    @Column(nullable = false)
    @Min(0) //품절인 경우도 고려
    private Integer stockQuantity;

    @Builder.Default //List<CategoryItem> 객체 타입 - 빌더 패턴 쓸때 null 방지를 위해
    //@ManyToMany : 다대다 관계는 실무에서 비추 category 하고 item은 다대다 관계이므로 중간 엔티티로 풀어냄
    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    //재고수량 증가 로직
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    //재고수량 감소 로직
    public void removeStock(int quantity) {
        int reststock = this.stockQuantity - quantity;
        if (reststock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = reststock;
    }
    //상품 수정
    public void updateInfo(UpdateBookRequest req){
        this.name = req.getName();
        this.price = req.getPrice();
        this.stockQuantity = req.getStockQuantity();


    }



}

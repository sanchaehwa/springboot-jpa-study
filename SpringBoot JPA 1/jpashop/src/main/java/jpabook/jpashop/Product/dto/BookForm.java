package jpabook.jpashop.Product.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookForm{
    private Long id;

    @NotEmpty(message = "책 이름 입력은 필수입니다.")
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "수량을 입력해주세요.")
    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    private Integer stockQuantity;

    private String author;
    private String isbn;
}

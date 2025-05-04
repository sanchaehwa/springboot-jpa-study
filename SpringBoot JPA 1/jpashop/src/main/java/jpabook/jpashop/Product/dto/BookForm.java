package jpabook.jpashop.Product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//폼 바인딩 객체는 Setter 주입
public class BookForm {
    private Long id;

    @NotBlank(message = "책 이름을 입력해주세요")  //NotBlank는 문자열만 가능
    private String name;
    @NotNull(message ="가격을 입력해주세요")
    private int price;
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다")
    private int stockQuantity;
    @NotBlank(message = "작가명을 입력해주세요")
    private String author;
    @NotBlank(message = "국제표준도서번호를 입력해주세요")
    private String isbn;
}

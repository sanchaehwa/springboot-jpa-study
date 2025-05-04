package jpabook.jpashop.Order.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.Member.domain.Address;
import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.Product.exception.NotEnoughStockException;
import jpabook.jpashop.orders.domain.Order;
import jpabook.jpashop.orders.model.OrderStatus;
import jpabook.jpashop.orders.repository.OrderRepository;

import jpabook.jpashop.Product.domain.Item;
import jpabook.jpashop.Product.domain.item.Book;
import jpabook.jpashop.orders.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class) //Junit5 이기에 Junit4 RunWith 대체
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문 생성 테스트")
    @Test
    public void order_product() throws Exception {
        //given
        //사용자 생성
        String username1 = "test1";
        String city1 = "Busan";
        String street1 = "Haeundae";
        String zipcode1 = "23-11";
        Member member = Member.builder()
                .username(username1)
                .address(new Address(city1, street1, zipcode1))
                .build();
        em.persist(member);

        //주문 생성
        String item_name = "Book";
        int item_price = 10000;
        int item_stock_quantity = 10;
        Book book = Book.builder()
                .name(item_name)
                .price(item_price)
                .stockQuantity(item_stock_quantity)
                .author("sam")
                .isbn("123")
                .build();
        em.persist(book);

        Item item = book;

        int OrderCount = 2; //주문 수량

        //when
        Long order_Id = orderService.order(member.getId(), item.getId(), OrderCount);

        //then
        Order getOrder = orderRepository.findById(order_Id);
        assertEquals(OrderStatus.ORDERED, getOrder.getStatus(), "상품 주문 상태는 주문 진행중"); //Junit5 부터는 메시지를 마지막에 넣어야함
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류수 ");
        assertEquals(10000 * OrderCount,getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야함 ");

    }

    @DisplayName("주문 취소 테스트")
    @Test
     public void order_cancel() throws Exception{
        Member member = createMember();
        String item_name = "Book";
        int item_price = 10000;
        int item_stock_quantity = 10;
        Item item = createBook(item_name, item_price, item_stock_quantity);
        int OrderCount = 2;
        Long order_Id = orderService.order(member.getId(), item.getId(), OrderCount);
       //when
        orderService.cancelOrder(order_Id);
        //then
        Order getOrder = orderRepository.findById(order_Id);
        assertEquals(OrderStatus.ORDER_CANCELED,getOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다.");
        assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해 야 한다.");
      }
      @Test
      public void order_Quantity_Exceeds_Stock_Should_Throw_Exception() throws Exception{
          //given
          Member member = createMember();
          String item_name = "Book1";
          int item_price = 10000;
          int item_stock_quantity = 10;
          Item item = createBook(item_name, item_price, item_stock_quantity);

          int orderCount = 11;
          //when/then
          assertThrows(
                  NotEnoughStockException.class,
                  () -> orderService.order(member.getId(), item.getId(),
                          orderCount));
       }
       private Member createMember(){
           //given
           String username2 = "test2";
           String city2 = "Busan";
           String street2 = "Haeundae";
           String zipcode2 = "23-11";
           Member member = Member.builder()
                   .username(username2)
                   .address(new Address(city2, street2, zipcode2))
                   .build();
           em.persist(member);
           return member;
       }
       private Book createBook(String item_name, int item_price, int item_stock_quantity){

           Book book = Book.builder()
                   .name(item_name)
                   .price(item_price)
                   .stockQuantity(item_stock_quantity)
                   .author("sam")
                   .isbn("123-456")
                   .build();
           em.persist(book);
           return book;
       }
          
         
        
}

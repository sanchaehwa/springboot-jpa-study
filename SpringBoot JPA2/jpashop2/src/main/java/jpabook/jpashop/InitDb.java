package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
조회용 샘플 데이터 입력
- userA
    - JPA1 Book
    - JPA2 Book
- userB
    - SPRING1 BOOK
    - SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;
    //의존형 주입이 완료되고나서, 초기화 콜백 적용 @ PostConstruct
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void dbInit1(){ //DB에 샘플데이터 저장하려는 매서드 *UserA

            Member member1 = createMember("userA","부산","서면","23-1" );
            em.persist(member1);

            Book book1 = createBook("JPA1 Book", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 Book", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 1);

            //주문자 생성
            Delivery delivery1 = createDelivery(member1);
            Order order1= Order.createOrder(member1, delivery1, orderItem1, orderItem2);
            em.persist(order1);

        }
        public void dbInit2(){
            Member member2 = createMember("userB","부산","해운대","23-2" );
            em.persist(member2);

            Book book1 = createBook("SPRING1 Book", 20000, 100);
            em.persist(book1);

            Book book2 = createBook("SPRING2 Book", 40000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 1);

            //주문자 생성
            Delivery delivery2 = createDelivery(member2);
            Order order2 = Order.createOrder(member2, delivery2, orderItem1, orderItem2);
            em.persist(order2);
        }



        //Member
        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
        //Book
        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }
        //Delivery
        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;

        }

    }

}

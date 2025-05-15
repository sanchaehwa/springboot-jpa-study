package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    //직접 DTO로 필요한 데이터를 나눠서 조회하는 방식

    public List<OrderQueryDto> findOrderQueryDtos() {
        //Order 정보 먼저 조회
        List<OrderQueryDto> result = findOrders(); //Query 1번  (ToOne 관계만 조회 / 주문 목록 조회)
        //먼저 조회한 Order에 해당하는 OrderItem을 조회해서 세팅
        result.forEach(o -> { //Query N번 (=> N+1 문제 발생)
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        " select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                " from Order o" +
                                " join o.member m" + //ToOne
                                " join o.delivery d", OrderQueryDto.class) //ToOne
                .getResultList();
    }//to one 관계라서 Join을 해도 데이터가 증가하지않음. (ex) 하나의 Item 만 가리킴.그러니깐 이 OrderItem 에 연결된 Item이 1개 이니깐, 떡 1개만 조인된다는것.
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        " select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId",
                        OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
        //to many 관계는 부모 하나에 여러 자식이 붙을수있으니깐 하나에 대한 여러 Row 반환 => 컬랙션 패치 조인을 써야함.
    }
}

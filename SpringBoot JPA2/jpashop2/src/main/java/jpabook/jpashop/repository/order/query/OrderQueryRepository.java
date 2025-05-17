package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    //컬렉션 패치 조인[v5]
    public List<OrderQueryDto> findAllByDto_optimization() {

        List<OrderQueryDto> result = findOrders();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId()))); //ForEach 하면 Void 형태라서 Return 문이랑 같이 쓸수없음
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        " select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" + //주문 먼저 조회 (페이징 가능) #1번 쿼리
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in : orderIds", //여러 개의 주문 ID들에 해당하는 주문 항목을 한 번에 조회하기 위해 In절 #2번 쿼리 *쿼리는 여러번 발생하지만 보통 2-3, 효율적 , 중복데이터 문제없음
                        // 컬렉션 패치 조인은 페이징 불가. 중복 데이터가 생기면 DISTINCT 해결
                        OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId)); //데이터 그룹핑 작업 (같은 주문 ID를 가진 OrderItemQueryDto를 List로 묶어 Map에 저장. - DB JOIN이 아닌 Java Memory JOIN (- 데이터 양이 많으면 성능 저하)
        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = getLongs(result);
        return orderIds;
    }

    private List<Long> getLongs(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
        return orderIds;
    }

    //플랫데이터 최적화[v8]
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new " +
                        " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) " +
                        " from Order o "+
                        " join o.member m " +
                        " join o.delivery d " +
                        " join o.orderItems oi " +
                        " join oi.item i",OrderFlatDto.class)
                .getResultList();
    } //중복은 생김. 주문 + 회원 + 배송 + 주문 상품 + 상품 정보 => 한줄로 펼쳐서 모두 데이터를 가져옴.(flat) , 복잡한 N+1 문제를 피하는데는 쓰임.
}

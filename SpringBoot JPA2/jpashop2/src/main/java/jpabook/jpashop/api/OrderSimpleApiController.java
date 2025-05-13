package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
xToOne(ManyToOne, OneToOne)
Order
Order -> MEMBER
Order -> DELIVERY
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() { //엔티티에서 조회
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() { //DTO에서 조회 -> 데이터가 필요할때마다 쿼리를 날림(Lazy 로딩) => N+1 문제 발생
      return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(SimpleOrderDto::new) //o -> new SimpleOrderDto
                .collect(Collectors.toList());
    }
//    @GetMapping("/api/v3/simple-orders") //성능 최적화 , 패치 조인
//    public List<SimpleOrderDto> ordersV3() {
//        List<Order> orders = orderRepository.findAllWithMemberDelivery();
//        return orders.stream()
//                .map(SimpleOrderDto::new)
//                .collect(Collectors.toList());
//
//    }
    @GetMapping("/api/v4/simple-orders") //JPA에서 DTO로 바로 가져오는것.
    public List<OrderSimpleQueryDto> ordersV4() {
       return orderSimpleQueryRepository.findOrderDtos();
    }




    //SampleOrderDTO
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}


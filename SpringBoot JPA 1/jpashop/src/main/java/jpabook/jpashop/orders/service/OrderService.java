package jpabook.jpashop.orders.service;

import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.Member.repository.MemberRepository;
import jpabook.jpashop.orders.domain.Delivery;
import jpabook.jpashop.orders.domain.Order;
import jpabook.jpashop.orders.domain.OrderItem;
import jpabook.jpashop.orders.model.DeliveryStatus;
import jpabook.jpashop.orders.model.OrderStatus;
import jpabook.jpashop.orders.repository.OrderRepository;
import jpabook.jpashop.Product.domain.Item;
import jpabook.jpashop.Product.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //회원 조회
        Member member = memberRepository.find(memberId);
        //상품 조회
        Item item = itemRepository.findOne_Item(itemId);
        //배송정보 생성
        Delivery delivery = Delivery.builder()
                .address(member.getAddress())
                .status(DeliveryStatus.DELIVERY_READY) //배송 준비
                .build();


        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(),count);
        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }
    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.cancelOrder();

    }
//    //검색
//    public List<Order> findOrders(OrderSearch  orderSearch) {
//        return orderRepository.findAll_Order(orderSearch);
//    }

}

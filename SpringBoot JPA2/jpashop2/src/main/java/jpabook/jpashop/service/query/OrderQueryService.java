package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static java.util.stream.Collectors.toList;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //조회만
public class OrderQueryService {
    private final OrderRepository orderRepository;

    public List<OrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithItem();  //LAZY 로딩을 피하고 필요한 연관 데이터 미리 로딩

        List<OrderDto> result = orders.stream() //Order 엔티티 하나하나를 OrderDto로 변환
                .map(o -> new OrderDto(o)) //이미 다 로딩된 상태이기에 OSIV 설정이 False여도 지연 로딩 문제 없이 작동
                .collect(toList());
        return result;
    }
}

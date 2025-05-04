package jpabook.jpashop.orders.controller;

import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.Member.service.MemberService;
import jpabook.jpashop.Product.domain.Item;
import jpabook.jpashop.Product.service.ItemService;
import jpabook.jpashop.orders.domain.Order;
import jpabook.jpashop.orders.dto.OrderSearch;
import jpabook.jpashop.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;
    //주문 생성 폼
    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);

        return "order/orderForm";
    }
    //실제주문
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId, @RequestParam("itemId") Long itemId, @RequestParam("count") int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }
    //주문 목록 검색
    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }
    //주문취소
    @PostMapping(value = "/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}

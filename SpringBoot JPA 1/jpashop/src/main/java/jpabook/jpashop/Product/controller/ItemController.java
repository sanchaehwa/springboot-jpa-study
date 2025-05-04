package jpabook.jpashop.Product.controller;


import jakarta.validation.Valid;
import jpabook.jpashop.Product.domain.Item;
import jpabook.jpashop.Product.domain.item.Book;
import jpabook.jpashop.Product.dto.BookForm;
import jpabook.jpashop.Product.dto.UpdateBookRequest;
import jpabook.jpashop.Product.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping(value = "/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("form") BookForm form,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "items/createItemForm";
        }

        Book book = Book.builder()
                .name(form.getName())
                .price(form.getPrice())
                .stockQuantity(form.getStockQuantity())
                .author(form.getAuthor())
                .isbn(form.getIsbn())
                .build();

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping()
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }
    @GetMapping(value = "{itemId}/edit")
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId); //처음 조회한 이 엔티티는 영속상태가 맞음

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        model.addAttribute("form", form);

        return "items/updateItemForm";
    }
    @PostMapping(value = "/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @Valid @ModelAttribute("form") BookForm form, BindingResult bindingResult, Model model) {
        //수정 시점에는 새로운 Book 객체를 만들고 있다 -> 준영속상태 (JPA가 원래 관리하던 기존 영속 엔티티와는 전혀 다른 객체)
        //새 객체를 만들어서 처리하는건 Merge() 동작, JPA의 엔티티 수정이 이루어지려면 항상 영속 상태인 엔티티를 조회해서 그 값만 변경해야함.

        /**
            Book book = Book.builder()
                    .name(form.getName())
                    .price(form.getPrice())
                    .stockQuantity(form.getStockQuantity())
                    .author(form.getAuthor())
                    .isbn(form.getIsbn())
                    .build();
            itemService.saveItem(book);
         **/
        //값이 입력되지않는 경우
        if (bindingResult.hasErrors()) {
            return "items/updateItemForm";
        }
        //파라미터로 받는 생성자 (final 불변객체로 사용하려면)
        UpdateBookRequest updateReq = new UpdateBookRequest(form.getName(),form.getPrice(),form.getStockQuantity());
        itemService.updateItem(itemId, updateReq); //기존영속엔티티(기존에 저장되어있던 부분)에 새로 수정한 값을 넣어줌

        return "redirect:/items";
    }
}

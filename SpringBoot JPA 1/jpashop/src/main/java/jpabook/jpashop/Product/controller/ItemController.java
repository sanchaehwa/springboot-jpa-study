package jpabook.jpashop.Product.controller;


import jakarta.validation.Valid;
import jpabook.jpashop.Product.domain.Item;
import jpabook.jpashop.Product.domain.item.Book;
import jpabook.jpashop.Product.dto.BookForm;
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
        Book item = (Book) itemService.find_item(itemId);
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
    public String updateItem(@PathVariable Long itemId, @Valid @ModelAttribute("form") BookForm form) {

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
}

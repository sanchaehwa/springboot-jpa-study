package jpabook.jpashop.Product.service;

import jpabook.jpashop.Product.domain.Item;
import jpabook.jpashop.Product.domain.item.Book;
import jpabook.jpashop.Product.dto.UpdateBookRequest;
import jpabook.jpashop.Product.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional //쓰기 작업도 해야하니깐 매서드 오버라이딩
    public void saveItem(Item item) {
        itemRepository.item_save(item);
    }

    @Transactional
    //item 정보 수정
    public void updateItem(Long itemId, UpdateBookRequest updateBookRequest) {
        //수정할 객체를 찾음
        Item item = itemRepository.findOne(itemId);
        //수정
        item.updateInfo(updateBookRequest);
        //JPA가 변경 감지가 되나깐 (Dirty Checking) 따로 저장안해도 됨
        // itemRepository.item_save(item);

    }


    //item 전체 조회
    public List<Item> findItems() {
        return itemRepository.findAll();

    }
    //item 하나 조회
    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

}

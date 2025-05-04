package jpabook.jpashop.Product.service;

import jpabook.jpashop.Product.domain.Item;
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


    //item 전체 조회
    public List<Item> findItems() {
        return itemRepository.findAll_item();

    }
    //item 하나 조회
    public Item find_item(Long id){
        return itemRepository.findOne_Item(id);
    }

}

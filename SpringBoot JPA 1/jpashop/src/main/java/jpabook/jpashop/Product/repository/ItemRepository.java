package jpabook.jpashop.Product.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.Product.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    //item 저장
    public void item_save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        }
        else {
            em.merge(item);
        }
     }
     //item 하나 조회
    public Item findOne_item(Long id) {
        return em.find(Item.class, id);
    }
    //item 전체 조회
    public List<Item> findAll_item() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}

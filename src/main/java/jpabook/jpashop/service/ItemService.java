package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 변경감지 방법 - 준영속 Context를 변경감지로 수정
    // 수정하고자 하는 속성만을 수정 -> 위험성 존재 X
    @Transactional // commit시 flush(영속성 context에서 변경 사항 확인 과정)를 통해 update query 생성
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        // 아래와 같이 수정하고자 하는 속성만 set을 통해 수정하는 것을 권장
        // 가장 좋은 방법은 의미있는 수정 메서드를 따로 생성해서 사용 - 유지보수에서 좋음
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }

    // 병합 방법(merge) - 아래 코드(실제 내부 구현 순서)를 em.merge로 jpa가 약식해준 것
    // 모든 속성이 변경됨 --> 값이 없을 때 null로 처리함(모든 속성을 변경하고자 할때는 merge 사용)
    /*@Transactional
    public Item updateItem(Long itemId, Book param) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        return findItem;
    } */

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }


}

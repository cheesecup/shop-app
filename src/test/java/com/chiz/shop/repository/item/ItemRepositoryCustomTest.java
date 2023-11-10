package com.chiz.shop.repository.item;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.domain.item.Item;
import com.chiz.shop.dto.item.ItemSearchDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class ItemRepositoryCustomTest {

    @Autowired ItemRepository itemRepository;

    @PersistenceContext EntityManager em;

    // 테스트용 상품 생성
    private void createItem() {
        for (int i=0; i<3; i++) {
            Item item = Item.of("판매 테스트 상품" + i, i * 1000, 100, "판매하고 있는 상품 " + i + "의 상세 내용", ItemSellStatus.SELL);
            itemRepository.save(item);
        }

        for (int i=0; i<3; i++) {
            Item item = Item.of("품절 테스트 상품" + i, i * 1000, 100, "품절된 상품 " + i + "의 상세 내용", ItemSellStatus.SOLD_OUT);
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 검색 테스트 - 판매 상태")
    void PRODUCT_SEARCH_TEST_SELL_STATUS() {
        //Given
        this.createItem();
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setSearchDateType("all");
        itemSearchDto.setSearchSellStatus(ItemSellStatus.SELL);
        itemSearchDto.setSearchBy("itemNm");
        itemSearchDto.setSearchQuery("테스트");

        Pageable pageable = PageRequest.of(0, 3);

        //When
        Page<Item> item = itemRepository.getAdminItemPage(itemSearchDto, pageable);

        //Then
        System.out.println(item.getContent().size());
    }

}
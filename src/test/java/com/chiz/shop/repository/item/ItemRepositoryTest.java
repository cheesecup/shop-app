package com.chiz.shop.repository.item;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.domain.item.Item;
import com.chiz.shop.domain.item.QItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    public void createItemList() {
        for (int i=1; i<=5; i++) {
            itemRepository.save(Item.of("test item" + i,
                    10000 * i,
                    100 * i,
                    "item" + i + " detail information",
                    ItemSellStatus.SELL));
        }
    }

    @Test
    @DisplayName("상품 저장 테스트")
    void TEST_ITEM_SAVE() {
        Item item = Item.of("test item",
                10000,
                100,
                "item detail information",
                ItemSellStatus.SELL);

        itemRepository.save(item);

        System.out.println(item);
    }

    @Test
    @DisplayName("상품 이름으로 검색 테스트")
    void TEST_SEARCH_BY_ITEM_NAME() {
        this.createItemList();
        List<Item> findItemList = itemRepository.findByItemNm("test item1");
        for (Item item : findItemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("상품 이름 또는 상세설명으로 검색 테스트")
    void TEST_SEARCH_PRODUCTS_BY_ITEM_NAME_OR_ITEM_DETAIL() {
        this.createItemList();
        List<Item> findItemList =
                itemRepository.findByItemNmOrItemDetail("test item1", "item3 detail information");
        for (Item item : findItemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("특정 가격보다 낮은 상품 검색 테스트")
    void TEST_SEARCH_PRODUCTS_BY_PRICE() {
        this.createItemList();
        List<Item> findItemList = itemRepository.findByPriceLessThan(30000);
        for (Item item : findItemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("특정 가격보다 낮은 상품 내림차순 검색 테스트")
    void TEST_SEARCH_PRODUCTS_DESC_ORDER_BY_PRICE() {
        this.createItemList();

        List<Item> findItemList = itemRepository.findByPriceLessThanOrderByPriceDesc(30000);
        for (Item item : findItemList) {
            System.out.println(item);
        }
    }
    
    @Test
    @DisplayName("입력한 상세설명이 포함된 상품 내림차순 검색 테스트")        
    void TEST_SEARCH_PRODUCTS_CONTAINING_ENTERED_WORD() {
        this.createItemList();

        List<Item> findItemList = itemRepository.findByItemDetailContainingOrderByPriceDesc("detail");
        for (Item item : findItemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("Querydsl 이용한 조회 테스트")
    void QUERY_TEST_USING_QUERYDSL() {
        this.createItemList();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        List<Item> findItemList = queryFactory.selectFrom(qItem)
                .where(qItem.itemDetail.like("%detail information%"))
                .fetch();

        for (Item item : findItemList) {
            System.out.println(item);
        }
    }


}
package com.chiz.shop.repository.item;

import com.chiz.shop.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemRepository  extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    // 상품 이름으로 검색
    List<Item> findByItemNm(String itemNm);

    // 입력한 조건이 포함된 상품 상세설명으로 검색
    List<Item> findByItemDetailContainingOrderByPriceDesc(String itemDetail);

    // 상품 이름 또는 상품 설명으로 검색
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    // 입력된 가격보다 낮은 상품 오름차순 검색
    List<Item> findByPriceLessThan(Integer price);

    // 입력된 가격보다 낮은 상품 내림차순 검색
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
}

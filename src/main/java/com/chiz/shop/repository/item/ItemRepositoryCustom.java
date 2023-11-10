package com.chiz.shop.repository.item;

import com.chiz.shop.domain.item.Item;
import com.chiz.shop.dto.item.ItemSearchDto;
import com.chiz.shop.dto.item.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    // 상품 관리 페이지 페이징
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    // 메인 화면 상품 페이징
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}

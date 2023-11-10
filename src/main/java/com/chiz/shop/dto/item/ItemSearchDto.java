package com.chiz.shop.dto.item;

import com.chiz.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemSearchDto {
    
    private String searchDateType; // 현재시간과 상품 등록시간을 비교해서 조회
    
    private ItemSellStatus searchSellStatus; // 판매 상태
    
    private String searchBy; // 조회 조건(유형)
    
    private String searchQuery = ""; // 조회할 검색어 저장
}

package com.chiz.shop.dto.response.item;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormResponse {

    private Long id;
    private String itemNm;
    private Integer price;
    private Integer stockNumber;
    private String itemDetail;
    private ItemSellStatus itemSellStatus;
    private List<ItemImgResponse> itemImgResponseList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();

    public ItemFormResponse() {}

    @Builder
    public ItemFormResponse(Long id, String itemNm, Integer price, Integer stockNumber, String itemDetail, ItemSellStatus itemSellStatus, List<ItemImgResponse> itemImgResponseList, List<Long> itemImgIds) {
        this.id = id;
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.itemImgResponseList = itemImgResponseList;
        this.itemImgIds = itemImgIds;
    }

    public static ItemFormResponse of(Item item) {
        return ItemFormResponse.builder()
                .id(item.getId())
                .itemNm(item.getItemNm())
                .price(item.getPrice())
                .stockNumber(item.getStockNumber())
                .itemDetail(item.getItemDetail())
                .itemSellStatus(item.getItemSellStatus())
                .build();
    }
}

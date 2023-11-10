package com.chiz.shop.dto.item;

import com.chiz.shop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {

    private Long id;

    private String itemNm;

    private Integer price;

    private Integer stockNumber;

    private String itemDetail;

    private String itemSellStatus;

    private String createdBy;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    public ItemDto() {}

    public ItemDto(Long id, String itemNm, Integer price, Integer stockNumber, String itemDetail, String itemSellStatus, String createdBy, LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.createdBy = createdBy;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }

    public static ItemDto of(Item item) {
        return new ItemDto(item.getId(),
                item.getItemNm(),
                item.getPrice(),
                item.getStockNumber(),
                item.getItemDetail(),
                item.getItemSellStatus().toString(),
                item.getCreatedBy(),
                item.getRegTime(),
                item.getUpdateTime());
    }
}

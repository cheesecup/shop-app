package com.chiz.shop.dto.item;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세 정보는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고수량은 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    public ItemFormDto() {}

    public ItemFormDto(Long id, String itemNm, Integer price, String itemDetail, Integer stockNumber, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.itemNm = itemNm;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
    }

    public ItemFormDto(String itemNm, Integer price, String itemDetail, Integer stockNumber, ItemSellStatus itemSellStatus) {
        this.itemNm = itemNm;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
    }

    public static ItemFormDto of(Item item) {
        return new ItemFormDto(item.getId(),
                item.getItemNm(),
                item.getPrice(),
                item.getItemDetail(),
                item.getStockNumber(),
                item.getItemSellStatus());
    }
}

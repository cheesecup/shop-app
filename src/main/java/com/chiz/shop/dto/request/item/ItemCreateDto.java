package com.chiz.shop.dto.request.item;

import com.chiz.shop.constant.ItemSellStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ItemCreateDto {

    @NotBlank(message = "상품명은 필수 입력입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력입니다.")
    @Max(value = 10000000, message = "입력 가능한 최대 가격은 일천만원입니다.")
    private Integer price;

    @NotNull(message = "수량은 필수 입력입니다.")
    @Max(value = 10000, message = "입력 가능한 최대 수량은 10000개 입니다.")
    private Integer stockNumber;

    @NotBlank(message = "상품에 대한 설명을 적어주세요.")
    private String itemDetail;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgCreateDto> itemImgCreateDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    public ItemCreateDto() {
    }

//    @Builder
//    public ItemCreateDto(String itemNm, Integer price, Integer stockNumber, String itemDetail, ItemSellStatus itemSellStatus, List<ItemImgCreateDto> itemImgCreateDtoList, List<Long> itemImgIds) {
//        this.itemNm = itemNm;
//        this.price = price;
//        this.stockNumber = stockNumber;
//        this.itemDetail = itemDetail;
//        this.itemSellStatus = itemSellStatus;
//        this.itemImgCreateDtoList = itemImgCreateDtoList;
//        this.itemImgIds = itemImgIds;
//    }

    @Builder
    public ItemCreateDto(String itemNm, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus) {
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
    }
}

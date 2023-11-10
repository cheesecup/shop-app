package com.chiz.shop.domain.item;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.domain.BaseEntity;
import com.chiz.shop.dto.item.ItemFormDto;
import com.chiz.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id = null;

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    protected Item() {
    }

    private Item(String itemNm, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus) {
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
    }

    public static Item of(String itemNm, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus) {
        return new Item(itemNm, price, stockNumber, itemDetail, itemSellStatus);
    }
    
    // 상품 등록 폼에 입력한 값을 받아 상품 정보를 생성하는 메서드
    public static Item createItem(ItemFormDto dto) {
        return Item.of(dto.getItemNm(), dto.getPrice(), dto.getStockNumber(), dto.getItemDetail(), dto.getItemSellStatus());
    }
    
    // 상품 수정 폼에 입력한 값을 받아 상품 정보를 수정하는 메서드
    public void updateItem(ItemFormDto dto) {
        this.itemNm = dto.getItemNm();
        this.price = dto.getPrice();
        this.stockNumber = dto.getStockNumber();
        this.itemDetail = dto.getItemDetail();
        this.itemSellStatus = dto.getItemSellStatus();
    }

    // 재고량 감소 메서드
    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;

        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + ")");
        }

        this.stockNumber = restStock;
    }

    // 재고량 증가 메서드
    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }

}

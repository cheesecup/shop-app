package com.chiz.shop.exception;

// 고객이 재고가 없는 상품 주문시 발생하는 예외
public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }
}

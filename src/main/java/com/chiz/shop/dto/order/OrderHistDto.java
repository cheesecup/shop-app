package com.chiz.shop.dto.order;

import com.chiz.shop.constant.OrderStatus;
import com.chiz.shop.domain.item.Orders;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 주문 정보를 담는 DTO
@Getter
@Setter
public class OrderHistDto {

    private Long orderId; // 주문 고유번호
    private String orderDate; // 주문 날짜
    private OrderStatus orderStatus; // 주문 상태
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>(); // 주문 상품 리스트

    public OrderHistDto(Orders order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}

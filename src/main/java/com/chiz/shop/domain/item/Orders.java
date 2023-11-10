package com.chiz.shop.domain.item;

import com.chiz.shop.constant.OrderStatus;
import com.chiz.shop.domain.BaseEntity;
import com.chiz.shop.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id = null;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    private LocalDateTime orderDate; // 주문 일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // `@OneToMany`의 fetch 기본값은 `LAZY`
    // 쉽게 보기위해 코드를 작성해 주었다
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Orders() {}

    private Orders(OrderStatus orderStatus, LocalDateTime orderDate, Member member, List<OrderItem> orderItems) {
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.member = member;
        this.orderItems = orderItems;
    }

    private Orders(Member member, List<OrderItem> orderItems) {
        this.member = member;
        this.orderItems = orderItems;
    }

    public static Orders of(OrderStatus orderStatus, LocalDateTime orderDate, Member member, List<OrderItem> orderItems) {
        return new Orders(orderStatus, orderDate, member, orderItems);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrders(this);
    }

    public static Orders createOrder(Member member, List<OrderItem> orderItems) {
        Orders orders = new Orders(OrderStatus.ORDER, LocalDateTime.now(), member, new ArrayList<>());
        for (OrderItem orderItem : orderItems) {
            orders.addOrderItem(orderItem);
        }

        return orders;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : this.orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }

    // 주문 취소
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}

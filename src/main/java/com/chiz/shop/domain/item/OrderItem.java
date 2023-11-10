package com.chiz.shop.domain.item;

import com.chiz.shop.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@Entity
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id = null;

    private int orderPrice; // 총 주문 가격
    private int count; // 주문 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    @Setter
    private Orders orders;

    protected OrderItem() {}

    private OrderItem(int orderPrice, int count, Item item, Orders orders) {
        this.orderPrice = orderPrice;
        this.count = count;
        this.item = item;
        this.orders = orders;
    }

    private OrderItem(Item item, int count, int orderPrice) {
        this.item = item;
        this.count = count;
        this.orderPrice = orderPrice;
    }

    private OrderItem(Orders orders) {
        this.orders = orders;
    }

    public static OrderItem of(int orderPrice, int count, Item item, Orders orders) {
        return new OrderItem(orderPrice, count, item, orders);
    }
    
    // 주문한 상품과 수량을 받아서 주문정보를 생성하는 메소드
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem(item, count, item.getPrice());

        item.removeStock(count);

        return orderItem;
    }
    
    // 주문한 상품의 총 가격을 계산하는 메소드
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }
}

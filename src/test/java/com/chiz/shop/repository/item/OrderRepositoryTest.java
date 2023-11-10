package com.chiz.shop.repository.item;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.constant.OrderStatus;
import com.chiz.shop.constant.Role;
import com.chiz.shop.domain.item.Item;
import com.chiz.shop.domain.item.OrderItem;
import com.chiz.shop.domain.item.Orders;
import com.chiz.shop.domain.member.Member;
import com.chiz.shop.repository.member.MemberRepository;
import com.chiz.shop.repository.order.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderRepositoryTest {

    @PersistenceContext EntityManager em;

    @Autowired ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired OrderItemRepository orderItemRepository;

    public Item createItem() {
        Item item = Item.of("테스트 상품", 35000, 100, "테스트 상품 상세 설명", ItemSellStatus.SELL);
        return itemRepository.save(item);
    }

    public Orders createOrder() {
        Member member = Member.of("chiz", "chiz@email.com", "12341234", "Seoul", Role.ADMIN);
        memberRepository.save(member);

        Orders order = Orders.of(OrderStatus.ORDER, LocalDateTime.now(), member, new ArrayList<>());

        for (int i=0; i<3; i++) {
            Item item = this.createItem();
            OrderItem orderItem = OrderItem.of(70000, 2, item, order);
            order.getOrderItems().add(orderItem);
        }

        return orderRepository.save(order);
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    void CASCADE_TEST() {
        Orders order = Orders.of(OrderStatus.ORDER, LocalDateTime.now(), null, new ArrayList<OrderItem>());

        for (int i=0; i<3; i++) {
            Item item = this.createItem();
            OrderItem orderItem = OrderItem.of(70000, 2, item, order);
            order.getOrderItems().add(orderItem);
        }

        // `saveAndFlush()`는 `save()`와 다르게 트랜잭션 내에서 즉시 `Data`를 `flush`하는 기능이다.
        // `save()`는 데이터가 바로 DB에 저장되지 않고 영속성 컨텍스트에 저장되었다가 `flush`나 `commit`이 실행되면 저장한다.
        orderRepository.saveAndFlush(order);
        em.clear();

        Orders savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(IllegalArgumentException::new);
        Assertions.assertThat(3).isEqualTo(savedOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("고아객체 테스트")
    void ORPHAN_REMOVAL_TEST() {
        Orders order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    void LAZY_LOADING_TEST() {
        Orders order = this.createOrder();
        Long firstOrderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(firstOrderItemId)
                .orElseThrow(IllegalArgumentException::new);

        System.out.println("Order class : " + orderItem.getOrders().getClass());
        System.out.println("========================");
        orderItem.getOrders().getOrderDate();
        System.out.println("========================");
    }
}
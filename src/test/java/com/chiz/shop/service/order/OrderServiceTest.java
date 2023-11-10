package com.chiz.shop.service.order;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.constant.OrderStatus;
import com.chiz.shop.constant.Role;
import com.chiz.shop.domain.item.Item;
import com.chiz.shop.domain.item.OrderItem;
import com.chiz.shop.domain.item.Orders;
import com.chiz.shop.domain.member.Member;
import com.chiz.shop.dto.order.OrderDto;
import com.chiz.shop.repository.item.ItemRepository;
import com.chiz.shop.repository.member.MemberRepository;
import com.chiz.shop.repository.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired MemberRepository memberRepository;

    public Item saveItem() {
        Item item = Item.of("테스트 상품", 10000, 100, "테스트 상품 상세 설명", ItemSellStatus.SELL);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = Member.of("고객", "user@email.com", "12341234", "남양주시", Role.USER);
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    void ORDER_TEST() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();
        int totalPrice = orderDto.getCount() * item.getPrice();

        assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
    }

    @Test
    @DisplayName("주문 취소 테스트")
    void ORDER_CANCEL_TEST() {
        // Given
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail());

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // When
        orderService.cancelOrder(orderId);

        // Then

        assertThat(OrderStatus.CANCEL).isEqualTo(order.getOrderStatus());
        assertThat(100).isEqualTo(item.getStockNumber());
    }

}
package com.chiz.shop.service.order;

import com.chiz.shop.domain.item.Item;
import com.chiz.shop.domain.item.ItemImg;
import com.chiz.shop.domain.item.OrderItem;
import com.chiz.shop.domain.item.Orders;
import com.chiz.shop.domain.member.Member;
import com.chiz.shop.dto.order.OrderDto;
import com.chiz.shop.dto.order.OrderHistDto;
import com.chiz.shop.dto.order.OrderItemDto;
import com.chiz.shop.repository.item.ItemImgRepository;
import com.chiz.shop.repository.item.ItemRepository;
import com.chiz.shop.repository.member.MemberRepository;
import com.chiz.shop.repository.order.OrderRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private JPAQueryFactory jpaQueryFactory;
    private final ItemImgRepository itemImgRepository;

    public OrderService(ItemRepository itemRepository,
                        MemberRepository memberRepository,
                        OrderRepository orderRepository,
                        EntityManager em,
                        ItemImgRepository itemImgRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.jpaQueryFactory = new JPAQueryFactory(em);
        this.itemImgRepository = itemImgRepository;
    }
    
    // 고객인 주문한 상품 아이디와 수량을 받아서 주문 정보 저장
    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Orders order = Orders.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    // 고객 주문 리스트 정보 조회
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrder(String email, Pageable pageable) {
        List<Orders> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtoList = new ArrayList<>();

        for (Orders order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtoList.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtoList, pageable, totalCount);
    }

    // 고객 정보와 주문 정보 검증 로직
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    // 주문 취소 로직
    public void cancelOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    // 장바구니에서 구매한 상품에 대한 주문 생성 로직
    public Long ordersInCart(List<OrderDto> orderDtoList, String email) {
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Orders order = Orders.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}

package com.chiz.shop.service.cart;

import com.chiz.shop.domain.item.Cart;
import com.chiz.shop.domain.item.CartItem;
import com.chiz.shop.domain.item.Item;
import com.chiz.shop.domain.member.Member;
import com.chiz.shop.dto.cart.CartDetailDto;
import com.chiz.shop.dto.cart.CartItemDto;
import com.chiz.shop.dto.cart.CartOrderDto;
import com.chiz.shop.dto.order.OrderDto;
import com.chiz.shop.repository.item.CartItemRepository;
import com.chiz.shop.repository.item.CartRepository;
import com.chiz.shop.repository.item.ItemRepository;
import com.chiz.shop.repository.member.MemberRepository;
import com.chiz.shop.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public CartService(ItemRepository itemRepository,
                       MemberRepository memberRepository,
                       CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       OrderService orderService) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderService = orderService;
    }

    // 장바구니에 상품을 담는 로직
    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    // 로그인한 회원의 정보를 이용하여 장바구니에 있는 상품 조회
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 장바구니에 상품이 없을 경우 빈 장바구니 반환
        if (cart == null) {
            return cartDetailDtoList;
        }

        return cartItemRepository.findCartDetailDtoList(cart.getId());
    }

    // 장바구니 수량 업데이트하는 로직
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    // 로그인한 회원과 장바구니 상품을 저장한 회원이 같은지 검증
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        // 로그인한 회원과 상품을 저장한 회원이 다를경우 `false`를 반환
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    // 장바구니에 넣은 상품 삭제하는 로직
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    // 주문 로직으로 전달할 `OrderDto` 리스트 생성 및 주문 로직 호출
    // 주문한 상품은 장바구니 제거
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.ordersInCart(orderDtoList, email);

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
}

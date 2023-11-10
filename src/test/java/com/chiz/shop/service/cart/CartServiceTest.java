package com.chiz.shop.service.cart;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.constant.Role;
import com.chiz.shop.domain.item.CartItem;
import com.chiz.shop.domain.item.Item;
import com.chiz.shop.domain.member.Member;
import com.chiz.shop.dto.cart.CartItemDto;
import com.chiz.shop.repository.item.CartItemRepository;
import com.chiz.shop.repository.item.ItemRepository;
import com.chiz.shop.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CartService cartService;
    @Autowired CartItemRepository cartItemRepository;

    public Item createItem() {
        Item item = Item.of("테스트 상품", 10000, 1000, "테스트 상품 상세 설명", ItemSellStatus.SELL);
        itemRepository.save(item);
        return item;
    }

    public Member createMember() {
        Member member = Member.of("test", "test@test.com", "12341234", "Korea", Role.ADMIN);
        memberRepository.save(member);
        return member;
    }

    @Test
    @DisplayName("장바구나 담기 테스트")
    void ADD_CART_TEST() {
        // Given
        Item item = this.createItem();
        Member member = this.createMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        // When
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        // Then
        assertThat(cartItem.getItem().getId()).isEqualTo(item.getId());
        assertThat(cartItem.getCount()).isEqualTo(cartItemDto.getCount());
    }
}
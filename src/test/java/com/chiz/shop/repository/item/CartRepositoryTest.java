package com.chiz.shop.repository.item;

import com.chiz.shop.constant.Role;
import com.chiz.shop.domain.item.Cart;
import com.chiz.shop.domain.member.Member;
import com.chiz.shop.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        Member member = Member.of("chiz", "chiz@email.com", "12341234", "Seoul", Role.ADMIN);
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 조회 테스트")
    void FIND_CART_AND_MEMBER_TEST() {
        Member member = this.createMember();

        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);

        // 조회 쿼리를 보기위한 코드
        // `persist()`를 하고 `em.find()`를 하면 조회 쿼리를 볼 수 없다
        // 테스트를 위한 코드를 제외하고는 거의 사용하지 않는다
        em.flush(); // DB에 데이터 반영
        em.clear(); // 영속석 컨텍스트 지우기
        // End

        Cart findCart = cartRepository.findById(cart.getId())
                .orElseThrow(IllegalArgumentException::new);
        assertThat(findCart.getMember().getId()).isEqualTo(member.getId());
    }
}
package com.chiz.shop.domain.item;

import com.chiz.shop.domain.BaseEntity;
import com.chiz.shop.domain.member.Member;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@Entity
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id = null;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Cart() {}

    private Cart(Member member) {
        this.member = member;
    }

    public static Cart createCart(Member member) {
        return new Cart(member);
    }
}

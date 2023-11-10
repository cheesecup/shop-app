package com.chiz.shop.repository.item;

import com.chiz.shop.domain.item.CartItem;
import com.chiz.shop.dto.cart.CartDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("SELECT new com.chiz.shop.dto.cart.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "FROM CartItem ci, ItemImg im " +
            "JOIN ci.item i " +
            "WHERE ci.cart.id = :cartId " +
            "AND im.item.id = ci.item.id " +
            "AND im.repImgYn = 'Y' " +
            "ORDER BY ci.regTime DESC")
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}

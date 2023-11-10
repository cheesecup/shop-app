package com.chiz.shop.repository.item;

import com.chiz.shop.domain.item.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

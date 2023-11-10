package com.chiz.shop.repository.order;

import com.chiz.shop.domain.item.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.member.email = :email ORDER BY o.orderDate DESC")
    List<Orders> findOrders(@Param("email") String email, Pageable pageable);

    @Query("SELECT count(o) FROM Orders o where o.member.email = :email")
    Long countOrder(@Param("email") String email);
}

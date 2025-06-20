package com.example.board.order;

import com.example.board.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByMerchantUid(String merchantUid);

    Optional<Order> findByIdAndUser(Long orderId, User user);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.item " +
            "WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);
}

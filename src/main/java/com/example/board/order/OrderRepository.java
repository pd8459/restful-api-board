package com.example.board.order;

import com.example.board.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByMerchantUid(String merchantUid);

    Optional<Order> findByIdAndUser(Long orderId, User user);
}

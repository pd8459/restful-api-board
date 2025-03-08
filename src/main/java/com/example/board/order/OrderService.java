package com.example.board.order;

import com.example.board.User.User;
import com.example.board.cart.Cart;
import com.example.board.cart.CartItem;
import com.example.board.cart.CartItemRepository;
import com.example.board.cart.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public OrderDto createOrder(User user) {

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("장바구니가 없습니다."));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("장바구니가 비어 있습니다.");
        }

        int totalAmount = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem(order, cartItem.getItem(), cartItem.getQuantity());
            order.addOrderItem(orderItem);
            totalAmount += cartItem.getItem().getPrice() * cartItem.getQuantity();
        }
        order.setTotalAmount(totalAmount);

        // 3️⃣ 주문 저장
        orderRepository.save(order);

        return new OrderDto(order.getId(), order.getTotalAmount(), order.getStatus());
    }


    public void deleteOrder(User user, Long orderId) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("해당 주문을 찾을 수 없습니다."));

        // 주문 삭제
        orderRepository.delete(order);
    }
}

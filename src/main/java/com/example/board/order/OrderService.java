package com.example.board.order;

import com.example.board.User.User;
import com.example.board.cart.Cart;
import com.example.board.cart.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;


    @Transactional
    public OrderDto createOrder(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new RuntimeException("장바구니를 찾을 수 없습니다."));
        if(cart.getCartItems().isEmpty()) {
            throw new RuntimeException("장바구니가 비어있습니다.");
        }
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(cartItem.getItem());
            orderItem.setQuantity(cartItem.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return new OrderDto(order, orderItems);
    }
}

package com.example.board.order;

import com.example.board.User.User;
import com.example.board.cart.Cart;
import com.example.board.cart.CartItem;
import com.example.board.cart.CartItemRepository;
import com.example.board.cart.CartRepository;
import com.example.board.item.Item;
import com.example.board.item.ItemRepository;
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
    private final ItemRepository itemRepository;

    public OrderDto createOrder(User user, OrderRequestDto dto) {
        // 주문 객체 생성
        Order order = new Order();
        order.setUser(user);
        order.setMerchantUid(dto.getMerchantUid());
        order.setImpUid(dto.getImpUid());
        order.setTotalAmount(dto.getTotalAmount());

        if (dto.getAddress() != null) {
            order.setPostcode(dto.getAddress().getPostcode());
            order.setRoadAddress(dto.getAddress().getRoadAddress());
            order.setJibunAddress(dto.getAddress().getJibunAddress());
            order.setDetailAddress(dto.getAddress().getDetailAddress());
            order.setExtraAddress(dto.getAddress().getExtraAddress());
        }

        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (OrderItemDto itemDto : dto.getItems()) {
                Item item = itemRepository.findById(itemDto.getItemId())
                        .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));
                new OrderItem(order, item, itemDto.getQuantity());
            }
        }

        System.out.println("Order items size: " + order.getOrderItems().size());
        Order savedOrder = orderRepository.save(order);
        return OrderDto.from(savedOrder);


    }

    public void deleteOrder(User user, Long orderId) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("해당 주문을 찾을 수 없습니다."));
        orderRepository.delete(order);
    }
}

package com.example.board.order;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        OrderDto orderDto = orderService.createOrder(user);
        return ResponseEntity.ok(orderDto);
    }
}

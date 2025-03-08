package com.example.board.order;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        try {
            OrderDto orderDto = orderService.createOrder(user);
            return ResponseEntity.ok(orderDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 주문 삭제 메서드
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@RequestParam String email, @PathVariable Long orderId) {
        // 사용자 정보 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        try {
            // 주문 삭제 서비스 호출
            orderService.deleteOrder(user, orderId);
            return ResponseEntity.ok("주문이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 삭제 실패: " + e.getMessage());
        }
    }
}

package com.example.board.order;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequestDto requestDto) {
        log.debug("Received OrderRequestDto: " + requestDto);

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        log.debug("Received impUid: " + requestDto.getImpUid());

        try {
            OrderDto orderDto = orderService.createOrder(user, requestDto);
            return ResponseEntity.ok(orderDto);
        } catch (Exception e) {
            log.error("Error creating order: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "서버 처리 실패: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@RequestParam String email, @PathVariable Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        try {
            orderService.deleteOrder(user, orderId);
            return ResponseEntity.ok("주문이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            log.error("Error deleting order: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("주문 삭제 실패: " + e.getMessage());
        }
    }


    @GetMapping("get/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetail(@PathVariable Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        OrderDto dto = OrderDto.from(order);
        return ResponseEntity.ok(dto);
    }







}

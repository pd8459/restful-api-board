package com.example.board.payment;

import com.example.board.order.Order;
import com.example.board.order.OrderRepository;
import com.example.board.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    @PostMapping("/complete")
    public ResponseEntity<Map<String, Object>> completePayment(@RequestBody PaymentRequest request) {
        Map<String, Object> response = new HashMap<>();

        // 1. Iamport에서 인증 토큰 받기
        String token = paymentService.getTokenFromIamport();
        if (token == null) {
            response.put("success", false);
            response.put("message", "Failed to get token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 2. 결제 검증
        boolean isValid = paymentService.verifyPayment(token, request.getImpUid(), request.getAmount());
        if (!isValid) {
            response.put("success", false);
            response.put("message", "Payment verification failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 3. 주문 정보 조회
        Order order = orderRepository.findByMerchantUid(request.getMerchantUid());
        if (order == null) {
            response.put("success", false);
            response.put("message", "Order not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // 4. 결제 성공 상태로 변경
        order.setStatus(OrderStatus.SUCCESS);
        orderRepository.save(order);

        response.put("success", true);
        response.put("message", "Payment completed successfully");
        return ResponseEntity.ok(response);
    }
}

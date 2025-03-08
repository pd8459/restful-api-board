package com.example.board.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/getToken")
    public ResponseEntity<String> getTokenFromIamport() {
        String token = paymentService.getTokenFromIamport();

        if ("서버 오류".equals(token) || "토큰 발급 실패".equals(token)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(token);
        }

        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate/{impUid}")
    public ResponseEntity<String> validatePayment(@PathVariable String impUid, @RequestParam String userEmail) {
        String result = paymentService.validatePayment(impUid, userEmail);
        return ResponseEntity.ok(result);
    }

}

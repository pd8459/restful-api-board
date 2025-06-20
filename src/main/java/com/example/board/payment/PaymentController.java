package com.example.board.payment;

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

    @PostMapping("/getToken")
    public ResponseEntity<String> getTokenFromIamport() {
        String token = paymentService.getTokenFromIamport();

        if ("서버 오류".equals(token) || "토큰 발급 실패".equals(token)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(token);
        }

        return ResponseEntity.ok(token);
    }


    @GetMapping("/validate/{impUid}")
    public ResponseEntity<PaymentValidationResponse> validatePayment(@PathVariable String impUid,
                                                                     @RequestParam String userEmail) {
        try {
            Long orderId = paymentService.validatePayment(impUid, userEmail);

            return ResponseEntity.ok(new PaymentValidationResponse(
                    true,
                    "결제 완료",
                    orderId
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(new PaymentValidationResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
    }




    @GetMapping("/test-keys")
    public String testKeys() {
        return "API Key: " + paymentService.getApiKey() + ", API Secret: " + paymentService.getApiSecret();
    }
}

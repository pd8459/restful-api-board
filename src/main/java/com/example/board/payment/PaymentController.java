package com.example.board.payment;

import com.example.board.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtUtil jwtUtil;


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
                                                                     HttpServletRequest request) {
        try {
            String email = getEmailFromRequest(request);
            Long orderId = paymentService.validatePayment(impUid, email);

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

    private String getEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null  || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("인증 토큰이 필요합니다.");
        }
        String token = authHeader.substring(7);
        return jwtUtil.validateAndGetEmail(token);
    }



    @GetMapping("/test-keys")
    public String testKeys() {
        return "API Key: " + paymentService.getApiKey() + ", API Secret: " + paymentService.getApiSecret();
    }
}

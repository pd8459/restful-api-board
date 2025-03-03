package com.example.board.payment;

import com.example.board.order.Order;
import com.example.board.order.OrderRepository;
import com.example.board.payment.PaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // application.properties 파일에서 값 가져오기
    @Value("${iamport.api.key}")
    private String API_KEY;

    @Value("${iamport.api.secret}")
    private String API_SECRET;

    /**
     * ✅ [STEP 1] 아임포트 토큰 발급
     */
    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getImportToken() throws JsonProcessingException {
        String url = "https://api.iamport.kr/users/getToken";

        // 요청 데이터
        Map<String, String> request = new HashMap<>();
        request.put("imp_key", API_KEY);
        request.put("imp_secret", API_SECRET);

        // ObjectMapper를 사용하여 Map을 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);

        // API 호출
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.get("response") != null) {
            Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");
            String accessToken = (String) responseData.get("access_token");

            // 액세스 토큰을 반환
            return ResponseEntity.ok(Map.of("access_token", accessToken));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "토큰 발급 실패"));
    }

    /**
     * ✅ [STEP 2] 결제 정보 조회
     */
    @GetMapping("/verify/{imp_uid}")
    public ResponseEntity<Map<String, Object>> getPaymentData(@PathVariable String imp_uid) {
        String token = getImportTokenFromIamport();
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "토큰 발급 실패"));
        }

        String url = "https://api.iamport.kr/payments/" + imp_uid;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return ResponseEntity.ok(response.getBody());
    }

    /**
     * ✅ [STEP 3] 결제 검증 및 주문 저장
     */
    @PostMapping("/complete")
    public ResponseEntity<Map<String, Object>> completePayment(@RequestBody PaymentRequest request) {
        Map<String, Object> response = new HashMap<>();

        // 아임포트에서 결제 정보 가져오기
        Map<String, Object> paymentData = getPaymentInfo(request.getImp_uid());
        if (paymentData == null) {
            response.put("success", false);
            response.put("message", "결제 정보를 찾을 수 없음");
            return ResponseEntity.badRequest().body(response);
        }

        Map<String, Object> responseData = (Map<String, Object>) paymentData.get("response");

        // 결제 금액 검증
        int amountPaid = (int) responseData.get("amount");
        if (amountPaid != request.getAmount()) {
            response.put("success", false);
            response.put("message", "결제 금액 불일치");
            return ResponseEntity.badRequest().body(response);
        }

        // 주문 저장
        Order order = new Order();
        order.setMerchantUid(request.getMerchant_uid());
        order.setImpUid(request.getImp_uid());
        order.setAmount(amountPaid);
        orderRepository.save(order);

        response.put("success", true);
        response.put("message", "결제 완료");
        return ResponseEntity.ok(response);
    }

    /**
     * 🛠 [유틸] 아임포트 토큰 발급 메서드
     */
    private String getImportTokenFromIamport() {
        String url = "https://api.iamport.kr/users/getToken";

        Map<String, String> request = new HashMap<>();
        request.put("imp_key", API_KEY);
        request.put("imp_secret", API_SECRET);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        Map<String, Object> body = response.getBody();

        if (body != null && body.get("response") != null) {
            Map<String, Object> responseBody = (Map<String, Object>) body.get("response");
            return (String) responseBody.get("access_token");
        }
        return null;
    }

    /**
     * 🛠 [유틸] imp_uid로 결제 정보 조회
     */
    private Map<String, Object> getPaymentInfo(String impUid) {
        String token = getImportTokenFromIamport();
        if (token == null) return null;

        String url = "https://api.iamport.kr/payments/" + impUid;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }
}

package com.example.board.payment;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Value("${iamport.api.key}")
    private String API_KEY;

    @Value("${iamport.api.secret}")
    private String API_SECRET;

    // 생성자에서 UserRepository 주입
    public PaymentService(RestTemplate restTemplate, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public String getTokenFromIamport() {
        String url = "https://api.iamport.kr/users/getToken";
        Map<String, String> body = new HashMap<>();
        body.put("imp_key", API_KEY);
        body.put("imp_secret", API_SECRET);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

                if ("0".equals(responseMap.get("code").toString())) {
                    Map<String, Object> responseData = (Map<String, Object>) responseMap.get("response");
                    return responseData != null ? responseData.get("access_token").toString() : "토큰 없음";
                } else {
                    return "토큰 발급 실패";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "서버 오류";
        }

        return "토큰 발급 실패";
    }

    public String validatePayment(String impUid, String userEmail) {
        String token = getTokenFromIamport();

        if (token == null) {
            return "토큰 발급 실패";
        }

        String url = "https://api.iamport.kr/payments/" + impUid;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);


                Map<String, Object> responseData = (Map<String, Object>) responseMap.get("response");


                User user = userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new RuntimeException("사용자 미발견"));

                Payment payment = Payment.builder()
                        .impUid((String) responseData.get("imp_uid"))
                        .merchantUid((String) responseData.get("merchant_uid"))
                        .pgProvider((String) responseData.get("pg_provider"))
                        .payMethod((String) responseData.get("pay_method"))
                        .amount(Long.parseLong(responseData.get("amount").toString()))
                        .status("paid")
                        .buyerName((String) responseData.get("buyer_name"))
                        .buyerEmail((String) responseData.get("buyer_email"))
                        .paidAt(Long.parseLong(responseData.get("paid_at").toString()))
                        .user(user)
                        .build();


                paymentRepository.save(payment);

                return "결제 완료";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "결제 검증 실패";
        }

        return "결제 검증 실패";
    }

}

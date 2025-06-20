package com.example.board.payment;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import com.example.board.order.Order;
import com.example.board.order.OrderRepository;
import com.example.board.payment.IamportTokenRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.board.order.OrderStatus;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Value("${iamport.api.key}")
    private String API_KEY;

    @Value("${iamport.api.secret}")
    private String API_SECRET;


    private String cachedToken;
    private long tokenExpiryTime;

    public synchronized String getTokenFromIamport() {
        System.out.println("API_KEY: " + API_KEY);
        System.out.println("API_SECRET: " + API_SECRET);

        long now = System.currentTimeMillis() / 1000;

        if (cachedToken != null && now < tokenExpiryTime) {
            return cachedToken;
        }

        String url = "https://api.iamport.kr/users/getToken";
        IamportTokenRequest request = new IamportTokenRequest(API_KEY, API_SECRET);

        try {
            String jsonPayload = new ObjectMapper().writeValueAsString(request);
            System.out.println("JSON Payload: " + jsonPayload);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<IamportTokenRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            System.out.println("Iamport API 응답 Body: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

                if ("0".equals(responseMap.get("code").toString())) {
                    Map<String, Object> responseData = (Map<String, Object>) responseMap.get("response");
                    cachedToken = responseData.get("access_token").toString();
                    tokenExpiryTime = Long.parseLong(responseData.get("expired_at").toString());

                    return cachedToken;
                } else {
                    throw new RuntimeException("토큰 발급 실패: " + responseMap.get("message"));
                }
            }
        } catch (Exception e) {
            System.err.println("토큰 발급 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Iamport 토큰 발급 실패", e);
        }

        throw new RuntimeException("Iamport 토큰 발급 실패");
    }


    @Transactional
    public Long validatePayment(String impUid, String userEmail) {
        String token = getTokenFromIamport();

        if (token == null) {
            throw new IllegalStateException("아임포트 토큰 발급 실패");
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

                long paidAmount = Long.parseLong(responseData.get("amount").toString());
                String merchantUid = (String) responseData.get("merchant_uid");

                System.out.println("API에서 받은 금액: " + paidAmount);
                System.out.println("merchantUid: " + merchantUid);

                Order order = orderRepository.findByMerchantUid(merchantUid)
                        .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

                if (order.getTotalAmount() != paidAmount) {
                    System.out.println("주문 금액: " + order.getTotalAmount());
                    throw new IllegalStateException("결제 금액 불일치");
                }

                User user = userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

                if (!order.getUser().getId().equals(user.getId())) {
                    throw new IllegalStateException("주문자와 유저 정보 불일치");
                }

                order.setStatus(OrderStatus.SUCCESS);

                return order.getId();
            } else {
                throw new IllegalStateException("결제 검증 실패: 아임포트 API 응답 오류");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("결제 검증 실패", e);
        }
    }




    public String getApiKey() {
        return API_KEY;
    }

    public String getApiSecret() {
        return API_SECRET;
    }
}

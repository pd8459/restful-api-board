package com.example.board.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate = new RestTemplate();

    // 직접 입력 방식으로 API 키와 비밀 키를 설정
    private final String API_KEY = "";  // 본인의 API KEY
    private final String API_SECRET = "";  // 본인의 API SECRET

    // Iamport에서 인증 토큰 받기
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

                if ("0".equals(responseMap.get("code").toString())) { // 응답 코드 0이 정상
                    Map<String, Object> responseData = (Map<String, Object>) responseMap.get("response");
                    return responseData != null ? responseData.get("access_token").toString() : null;
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 결제 검증
    public boolean verifyPayment(String token, String impUid, int amount) {
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

                if ("0".equals(responseMap.get("code").toString())) { // 응답 코드 0이 정상
                    Map<String, Object> paymentData = (Map<String, Object>) responseMap.get("response");
                    int paidAmount = Integer.parseInt(paymentData.get("amount").toString());
                    return paidAmount == amount;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

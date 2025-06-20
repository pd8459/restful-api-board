package com.example.board.order;

import com.example.board.User.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private String merchantUid;
    private int totalAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    private String status;
    private String email;

    // 배송지 정보
    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;
    private String extraAddress;

    // 주문 아이템 리스트
    private List<OrderItemDto> items;

    public static OrderDto from(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setMerchantUid(order.getMerchantUid());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());
        dto.setEmail(order.getUser().getEmail());

        // 배송지 정보 세팅
        dto.setPostcode(order.getPostcode());
        dto.setRoadAddress(order.getRoadAddress());
        dto.setJibunAddress(order.getJibunAddress());
        dto.setDetailAddress(order.getDetailAddress());
        dto.setExtraAddress(order.getExtraAddress());

        // 주문 아이템 리스트 변환
        dto.setItems(order.getOrderItems().stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList()));

        return dto;
    }
}

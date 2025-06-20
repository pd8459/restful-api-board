package com.example.board.order;

import com.example.board.payment.AddressDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private String email;

    @JsonProperty("merchantUid")
    private String merchantUid;

    private int totalAmount;

    @JsonProperty("impUid")
    private String impUid;

    private AddressDto address;

    private List<OrderItemDto> items;
}

package com.example.board.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;
    private String extraAddress;
}

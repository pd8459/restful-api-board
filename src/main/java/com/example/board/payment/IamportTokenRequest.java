package com.example.board.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IamportTokenRequest {

    @JsonProperty("imp_key")
    private String impKey;

    @JsonProperty("imp_secret")
    private String impSecret;

    public IamportTokenRequest(String impKey, String impSecret) {
        this.impKey = impKey;
        this.impSecret = impSecret;
    }

    public String getImpKey() {
        return impKey;
    }

    public String getImpSecret() {
        return impSecret;
    }
}

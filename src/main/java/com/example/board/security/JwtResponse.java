package com.example.board.security;

import lombok.Getter;

@Getter
public class JwtResponse {
    private final String token;
    private final String email;

    public JwtResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}

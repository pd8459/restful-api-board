package com.example.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class RestfulApiBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestfulApiBoardApplication.class, args);
    }

}

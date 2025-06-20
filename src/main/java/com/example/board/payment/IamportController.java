package com.example.board.payment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IamportController {

    @GetMapping("/iamport")
    public String showIamportPage() {
        return "iamport";
    }
}

package com.example.board.Home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartPageController {

    @GetMapping("/cart")
    public String loginPage() {
        return "cart";
    }

}

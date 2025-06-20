package com.example.board.Home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderPageController {

    @GetMapping("/order")
    public String orderpage() {
        return "order";
    }

}

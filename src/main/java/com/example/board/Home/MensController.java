package com.example.board.Home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MensController {

    @GetMapping("/mens_category")
    public String mensCategory() {
        return "mens_category";
    }
}

package com.example.board.Home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WomensController {

    @GetMapping("/womens_category")
    public String womensCategory() {
        return "womens_category";
    }
}
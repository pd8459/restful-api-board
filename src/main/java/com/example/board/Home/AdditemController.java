package com.example.board.Home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdditemController {

    @GetMapping("/add_item")
    public String addItemPage() {
        return "add_item";
    }
}

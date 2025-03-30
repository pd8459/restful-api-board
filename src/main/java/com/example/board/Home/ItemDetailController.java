package com.example.board.Home;

import com.example.board.item.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/itemdetail")
public class ItemDetailController {

    private final ItemService itemService;

    public ItemDetailController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public String itemDetail(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "item_detail";
    }
}


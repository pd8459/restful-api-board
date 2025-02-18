package com.example.board.cart;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    private List<CartItemDto> getCartItems(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return cartService.getCartItems(user);
    }

    @PostMapping("/add")
    public void addItemToCart(@RequestParam String email, @RequestParam Long itemId, @RequestParam int quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        cartService.addItemToCart(email, itemId, quantity);
    }

    @PostMapping("/update")
    public void updateCartItem(@RequestParam String email, @RequestParam Long cartItemId, @RequestParam int quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다"));
        cartService.updateCartItem(user,cartItemId,quantity);
    }

    @DeleteMapping("/remove")
    public void removeCartItem(@RequestParam String email, @RequestParam Long cartItemId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다"));
        cartService.removeCartItem(user, cartItemId);
    }

}

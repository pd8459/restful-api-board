package com.example.board.cart;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> addItemToCart(@RequestBody CartRequest cartRequest) {
        try {
            User user = userRepository.findByEmail(cartRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            cartService.addItemToCart(user, cartRequest.getItemId(), cartRequest.getQuantity());

            Map<String, String> response = new HashMap<>();
            response.put("message", "장바구니에 추가되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "장바구니 추가 실패: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update")
    public void updateCartItem(@RequestParam String email, @RequestParam Long cartItemId, @RequestParam int quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        cartService.updateCartItem(user, cartItemId, quantity);
    }

    @DeleteMapping("/remove")
    public void removeCartItem(@RequestParam String email, @RequestParam Long cartItemId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        cartService.removeCartItem(user, cartItemId);
    }
}

package com.example.board.cart;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import com.example.board.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private String getEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("인증 토큰이 필요합니다.");
        }
        String token = authHeader.substring(7);
        return jwtUtil.validateAndGetEmail(token);
    }

    @GetMapping
    public ResponseEntity<?> getCartItems(HttpServletRequest request) {
        try {
            String email = getEmailFromRequest(request);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            List<CartItemDto> items = cartService.getCartItems(user);
            return ResponseEntity.ok(items);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody CartRequest cartRequest, HttpServletRequest request) {
        try {
            String email = getEmailFromRequest(request);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            cartService.addItemToCart(user, cartRequest.getItemId(), cartRequest.getQuantity());
            return ResponseEntity.ok(Map.of("message", "장바구니에 추가되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestParam Long cartItemId,
                                            @RequestParam int quantity,
                                            HttpServletRequest request) {
        try {
            String email = getEmailFromRequest(request);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            cartService.updateCartItem(user, cartItemId, quantity);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeCartItem(@RequestParam Long cartItemId,
                                            HttpServletRequest request) {
        try {
            String email = getEmailFromRequest(request);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            cartService.removeCartItem(user, cartItemId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}


package com.example.board.cart;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import com.example.board.item.Item;
import com.example.board.item.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<CartItemDto> getCartItems(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));
        return cart.getCartItems().stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addItemToCart(User user, Long itemId, int quantity) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalAmount(0);
                    return cartRepository.save(newCart);
                });

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        CartItem cartItem = cartItemRepository.findByCartAndItem(cart, item)
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setCart(cart);
                    newCartItem.setItem(item);
                    newCartItem.setQuantity(0);
                    return newCartItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);

        cart.recalculateTotalAmount();
        cartRepository.save(cart);
    }




    // 장바구니 아이템 수량 업데이트
    public void updateCartItem(User user, Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));
        cartItem.updateQuantity(quantity); // 수량 업데이트
        cartItemRepository.save(cartItem);
    }

    // 장바구니 아이템 제거
    public void removeCartItem(User user, Long cartItemId) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));

        cart.removeCartItem(cartItem); // 아이템 제거
        cartItemRepository.delete(cartItem); // DB에서 아이템 삭제

        // 장바구니 총 금액 재계산
        cart.recalculateTotalAmount(); // 총 금액 업데이트
        cartRepository.save(cart); // 카트 정보 업데이트
    }
}

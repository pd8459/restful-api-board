package com.example.board.cart;

import com.example.board.User.User;
import com.example.board.User.UserRepository;
import com.example.board.item.Item;
import com.example.board.item.ItemRepository;
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

    public void addItemToCart(String email, Long itemId, int quantity) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
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
    }

    public void updateCartItem(User user, Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));
        cartItem.updateQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public void removeCartItem(User user, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));
        cartItemRepository.delete(cartItem);
    }
}

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
    public void addItemToCart(String email, Long itemId, int quantity) {
        // 유저 정보 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 유저의 장바구니 찾기 또는 새로 생성
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    // 새 장바구니를 생성할 때 totalAmount를 0으로 설정
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalAmount(0); // 총액 초기화
                    return cartRepository.save(newCart); // 새로운 장바구니 저장
                });

        // 아이템 정보 찾기
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 장바구니에 해당 아이템이 있는지 확인, 없으면 새로 생성
        CartItem cartItem = cartItemRepository.findByCartAndItem(cart, item)
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setCart(cart);
                    newCartItem.setItem(item);
                    newCartItem.setQuantity(0);  // 새 아이템의 초기 수량은 0
                    return newCartItem;
                });

        // 아이템 수량 업데이트
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem); // 장바구니 아이템 저장

        // 장바구니의 총액 갱신
        cart.addCartItem(cartItem); // addCartItem 메서드를 호출하여 총액 갱신

        // 총액 갱신 후 장바구니 저장
        cartRepository.save(cart); // DB에 장바구니 저장
    }





    public void updateCartItem(User user, Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));
        cartItem.updateQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public void removeCartItem(User user, Long cartItemId) {
        // 사용자 장바구니 가져오기
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));

        // 삭제할 CartItem 찾기
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));

        // CartItem 제거
        cart.removeCartItem(cartItem);

        // CartItem 삭제
        cartItemRepository.delete(cartItem);

        // 장바구니 총액 갱신
        cart.recalculateTotalAmount();

        // 변경된 장바구니 저장
        cartRepository.save(cart);
    }



}

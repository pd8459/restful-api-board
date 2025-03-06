package com.example.board.cart;

import com.example.board.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();

    // 총액을 저장하는 필드
    private int totalAmount;

    // CartItem이 추가되거나 삭제될 때 총액을 갱신
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        recalculateTotalAmount();
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        recalculateTotalAmount();
    }

    // 총액을 재계산하는 메서드
    public void recalculateTotalAmount() {
        this.totalAmount = cartItems.stream()
                .mapToInt(cartItem -> cartItem.getItem().getPrice() * cartItem.getQuantity())
                .sum();
    }
}

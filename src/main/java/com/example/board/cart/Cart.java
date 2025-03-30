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

    private int totalAmount;

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        recalculateTotalAmount();
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        recalculateTotalAmount();
    }

    public void recalculateTotalAmount() {
        this.totalAmount = cartItems.stream()
                .mapToInt(cartItem -> cartItem.getItem().getPrice() * cartItem.getQuantity())
                .sum();
    }
}

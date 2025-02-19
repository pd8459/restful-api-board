package com.example.board.order;

import com.example.board.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private int quantity;
    private int price;

    public OrderItem(Order order, Item item,  int quantity) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.price = item.getPrice() * quantity;
    }
}

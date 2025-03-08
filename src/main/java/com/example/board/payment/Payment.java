package com.example.board.payment;

import com.example.board.User.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String impUid;
    private String merchantUid;
    private String pgProvider;
    private String payMethod;
    private Long amount;
    private String status;
    private String buyerName;
    private String buyerEmail;
    private Long paidAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

package com.web.shop_ttcs.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "cartItem")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date orderDate;
    private Date deliveryDate;
    private Long quantity;
    private String status;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;
}

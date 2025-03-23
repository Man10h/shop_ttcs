package com.web.shop_ttcs.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;
    private String category;
    private Long quantity;
    private Long numberOfRate;
    private Double totalOfRate;
    private Double rating;

    @OneToMany(mappedBy = "productEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<CartItemEntity> cartItemEntities;

    @ManyToOne
    @JoinColumn(name = "shopId")
    private ShopEntity shopEntity;

    @OneToMany(mappedBy = "productEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ImageEntity> imageEntities;
}

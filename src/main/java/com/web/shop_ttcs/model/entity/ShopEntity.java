package com.web.shop_ttcs.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "shop")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String type;
    private Double rating;

    @ManyToMany(mappedBy = "shopEntities")
    private List<UserEntity> userEntities;

    @OneToMany(mappedBy = "shopEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ProductEntity> productEntities;

    @ManyToOne
    private UserEntity userEntity;
}

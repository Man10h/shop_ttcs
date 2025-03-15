package com.web.shop_ttcs.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Long age;
    private String sex;
    private String phoneNumber;
    private String email;
    private Long coins;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ImageEntity> imageEntities;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private RoleEntity role;

    @ManyToMany
    @JoinTable(name = "user_product",
        joinColumns = @JoinColumn(name = "userId", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "productId", nullable = false))
    private List<ProductEntity> productEntities;

    @ManyToMany
    @JoinTable(name = "user_shop",
        joinColumns = @JoinColumn(name = "userId", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "shopId", nullable = false))
    private List<ShopEntity> shopEntities;


}

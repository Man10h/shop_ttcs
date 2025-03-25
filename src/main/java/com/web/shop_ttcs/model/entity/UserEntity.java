package com.web.shop_ttcs.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // basic attributes
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Long age;
    private String sex;
    private String phoneNumber;
    private String email;
    private Long coins;
    //
    private Boolean enabled;
    private String verificationCode;
    private Date verificationCodeExpiration;

    // relational
    @OneToMany(mappedBy = "userEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ImageEntity> imageEntities;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private RoleEntity role;

    @OneToMany(mappedBy = "userEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<CartItemEntity> cartItemEntities;

    @ManyToMany
    @JoinTable(name = "user_shop",
        joinColumns = @JoinColumn(name = "userId", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "shopId", nullable = false))
    private List<ShopEntity> shopEntities;

    @OneToMany(mappedBy = "userEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<RefreshTokenEntity> refreshTokenEntities;

    @OneToMany(mappedBy = "userEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<RatingEntity> ratingEntities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getName());
        authorities.add(grantedAuthority);
        return authorities;
    }
}

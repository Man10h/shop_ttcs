package com.web.shop_ttcs.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refreshToken")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;
}

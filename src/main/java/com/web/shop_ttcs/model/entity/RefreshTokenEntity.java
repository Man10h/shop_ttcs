package com.web.shop_ttcs.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;
}

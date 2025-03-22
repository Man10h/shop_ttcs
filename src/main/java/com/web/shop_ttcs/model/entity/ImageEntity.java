package com.web.shop_ttcs.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String name;
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;
}

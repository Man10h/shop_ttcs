package com.web.shop_ttcs.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "role")
    private List<UserEntity> userEntities;
}

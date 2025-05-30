package com.web.shop_ttcs.repository;

import com.web.shop_ttcs.model.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    @Override
    @EntityGraph(value = "user-ownShop-role-refreshToken-image", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserEntity> findById(Long id);
}

package com.web.shop_ttcs.repository;

import com.web.shop_ttcs.model.entity.RefreshTokenEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    void deleteByUserEntity(UserEntity userEntity);
    public Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}

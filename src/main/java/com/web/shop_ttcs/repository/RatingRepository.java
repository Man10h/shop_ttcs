package com.web.shop_ttcs.repository;

import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.RatingEntity;
import com.web.shop_ttcs.model.entity.ShopEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    Optional<RatingEntity> findByUserEntityAndProductEntity(UserEntity userEntity, ProductEntity productEntity);
    List<RatingEntity> findByUserEntity(UserEntity userEntity);
    List<RatingEntity> findByProductEntity_Id(Long productId);
}

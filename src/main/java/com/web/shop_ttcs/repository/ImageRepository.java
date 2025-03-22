package com.web.shop_ttcs.repository;

import com.web.shop_ttcs.model.entity.ImageEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    void deleteByUserEntity(UserEntity userEntity);
}

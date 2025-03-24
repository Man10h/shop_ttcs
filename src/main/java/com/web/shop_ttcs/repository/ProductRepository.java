package com.web.shop_ttcs.repository;

import com.web.shop_ttcs.model.entity.CartItemEntity;
import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.repository.custom.ProductCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductCustom {
    List<ProductEntity> findByShopEntity_Id(Long shopId);
}

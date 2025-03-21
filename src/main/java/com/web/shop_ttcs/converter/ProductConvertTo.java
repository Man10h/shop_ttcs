package com.web.shop_ttcs.converter;

import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.response.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConvertTo {

    @Autowired
    private ModelMapper modelMapper;

    public  ProductResponse convertTo(ProductEntity productEntity) {
        ProductResponse productResponse = modelMapper.map(productEntity, ProductResponse.class);
        productResponse.setShopId(productEntity.getShopEntity().getId());
        return productResponse;
    }
}

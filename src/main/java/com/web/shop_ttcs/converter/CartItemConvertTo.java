package com.web.shop_ttcs.converter;

import com.web.shop_ttcs.model.entity.CartItemEntity;
import com.web.shop_ttcs.model.enums.CartItemStatus;
import com.web.shop_ttcs.model.response.CartItemResponse;
import com.web.shop_ttcs.model.response.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartItemConvertTo {

    @Autowired
    private ProductConvertTo productConvertTo;

    @Autowired
    private ModelMapper modelMapper;

    public CartItemResponse convertTo(CartItemEntity cartItemEntity) {
        CartItemResponse cartItemResponse = modelMapper.map(cartItemEntity, CartItemResponse.class);
        ProductResponse productResponse = productConvertTo.convertTo(cartItemEntity.getProductEntity());
        cartItemResponse.setProductResponse(productResponse);

        if(!cartItemEntity.getStatus().isEmpty()){
            cartItemResponse.setStatus(CartItemStatus.toMap().get(cartItemEntity.getStatus()));
        }
        return cartItemResponse;
    }
}

package com.web.shop_ttcs.converter;

import com.web.shop_ttcs.model.dto.ShopDTO;
import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.ShopEntity;
import com.web.shop_ttcs.model.enums.Type;
import com.web.shop_ttcs.model.response.ProductResponse;
import com.web.shop_ttcs.model.response.ShopResponse;
import jakarta.persistence.Column;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ShopConvertTo {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductConvertTo productConvertTo;


    public ShopResponse convertTo(ShopEntity shopEntity) {
        ShopResponse shopResponse = modelMapper.map(shopEntity, ShopResponse.class);
        List<ProductEntity> productEntities = shopEntity.getProductEntities();
        List<ProductResponse> productResponses = new ArrayList<>();
        for(ProductEntity productEntity : productEntities) {
            productResponses.add(productConvertTo.convertTo(productEntity));
        }
        shopResponse.setProductResponses(productResponses);
        //
        if(!shopEntity.getType().isEmpty()){
            shopResponse.setType(Type.toMap().get(shopEntity.getType()));
        }
        return shopResponse;
    }
}

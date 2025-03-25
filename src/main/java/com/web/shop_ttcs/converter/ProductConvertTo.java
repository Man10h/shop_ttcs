package com.web.shop_ttcs.converter;

import com.web.shop_ttcs.model.entity.ImageEntity;
import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.RatingEntity;
import com.web.shop_ttcs.model.enums.Category;
import com.web.shop_ttcs.model.response.ImageResponse;
import com.web.shop_ttcs.model.response.ProductResponse;
import com.web.shop_ttcs.model.response.RatingResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductConvertTo {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageConvertTo imageConvertTo;


    // find/search Product
    public  ProductResponse convertTo(ProductEntity productEntity) {
        ProductResponse productResponse = modelMapper.map(productEntity, ProductResponse.class);
        productResponse.setShopId(productEntity.getShopEntity().getId());
        if(!productEntity.getCategory().isEmpty()){
            productResponse.setCategory(Category.toMap().get(productEntity.getCategory()));
        }
        List<ImageResponse> imageResponses = new ArrayList<>();
        for(ImageEntity imageEntity: productEntity.getImageEntities()){
            imageResponses.add(imageConvertTo.convertTo(imageEntity));
        }
        productResponse.setImageResponses(imageResponses);
        return productResponse;
    }

}

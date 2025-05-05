package com.web.shop_ttcs.converter;

import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.RatingEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.model.response.ProductRatingResponse;
import com.web.shop_ttcs.model.response.RatingResponse;
import com.web.shop_ttcs.model.response.UserRatingResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingConvertTo {


    @Autowired
    private ModelMapper modelMapper;

    //for seeing product
    public RatingResponse convertToComment(RatingEntity ratingEntity) {
        RatingResponse ratingResponse = modelMapper.map(ratingEntity, RatingResponse.class);
        UserEntity userEntity = ratingEntity.getUserEntity();
        UserRatingResponse userRatingResponse = modelMapper.map(userEntity, UserRatingResponse.class);
        userRatingResponse.setUrl(!userEntity.getImageEntities().isEmpty() ? userEntity.getImageEntities().get(0).getUrl() : null);
        ratingResponse.setUserRatingResponse(userRatingResponse);
        return ratingResponse;
    }


    // for seeing user's rating
    public RatingResponse convertToRating(RatingEntity ratingEntity) {
        RatingResponse ratingResponse = modelMapper.map(ratingEntity, RatingResponse.class);
        ProductEntity productEntity = ratingEntity.getProductEntity();
        ProductRatingResponse productRatingResponse = modelMapper.map(productEntity, ProductRatingResponse.class);
        productRatingResponse.setUrl(!productEntity.getImageEntities().isEmpty() ? productEntity.getImageEntities().get(0).getUrl() : null);
        ratingResponse.setProductRatingResponse(productRatingResponse);
        return ratingResponse;
    }
}

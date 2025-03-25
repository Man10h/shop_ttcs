package com.web.shop_ttcs.converter;

import com.web.shop_ttcs.model.entity.RatingEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.model.response.RatingResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingConvertTo {

    @Autowired
    private UserConvertTo userConvertTo;

    @Autowired
    private ProductConvertTo productConvertTo;

    @Autowired
    private ModelMapper modelMapper;

    //for seeing product
    public RatingResponse convertToComment(RatingEntity ratingEntity) {
        RatingResponse ratingResponse = modelMapper.map(ratingEntity, RatingResponse.class);
        ratingResponse.setUserResponse(userConvertTo.convertTo(ratingEntity.getUserEntity()));
        return ratingResponse;
    }


    // for seeing user's rating
    public RatingResponse convertToRating(RatingEntity ratingEntity) {
        RatingResponse ratingResponse = modelMapper.map(ratingEntity, RatingResponse.class);
        ratingResponse.setProductResponse(productConvertTo.convertTo(ratingEntity.getProductEntity()));
        return ratingResponse;
    }
}

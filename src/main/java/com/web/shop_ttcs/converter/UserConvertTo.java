package com.web.shop_ttcs.converter;


import com.web.shop_ttcs.model.entity.ImageEntity;
import com.web.shop_ttcs.model.entity.RefreshTokenEntity;
import com.web.shop_ttcs.model.entity.ShopEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.model.response.ImageResponse;
import com.web.shop_ttcs.model.response.RefreshTokenResponse;
import com.web.shop_ttcs.model.response.ShopResponse;
import com.web.shop_ttcs.model.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConvertTo {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageConvertTo imageConvertTo;

    @Autowired
    private RefreshTokenConvertTo refreshTokenConvertTo;

    @Autowired
    private ShopConvertTo shopConvertTo;

    public UserResponse convertTo(UserEntity userEntity) {
        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
        userResponse.setRoleName(userEntity.getRole().getName());
        // image convert
        List<ImageResponse> imageResponses = new ArrayList<>();
        List<ImageEntity> imageEntities = userEntity.getImageEntities();
        for (ImageEntity imageEntity : imageEntities) {
            imageResponses.add(imageConvertTo.convertTo(imageEntity));
        }
        userResponse.setImages(imageResponses);
        // refreshToken convert
        List<RefreshTokenResponse> refreshTokenResponses = new ArrayList<>();
        List<RefreshTokenEntity> refreshTokenEntities = userEntity.getRefreshTokenEntities();
        for(RefreshTokenEntity refreshTokenEntity : refreshTokenEntities) {
            refreshTokenResponses.add(refreshTokenConvertTo.convertTo(refreshTokenEntity));
        }
        userResponse.setRefreshTokens(refreshTokenResponses);

        //shops convert
        List<ShopResponse> shopResponses = new ArrayList<>();
        List<ShopEntity> shopEntities = userEntity.getShopEntities();
        for(ShopEntity shopEntity : shopEntities) {
            shopResponses.add(shopConvertTo.convertTo(shopEntity));
        }
        userResponse.setShops(shopResponses);

        return userResponse;
    }
}

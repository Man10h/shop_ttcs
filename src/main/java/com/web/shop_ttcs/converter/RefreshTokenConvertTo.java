package com.web.shop_ttcs.converter;

import com.web.shop_ttcs.model.entity.RefreshTokenEntity;
import com.web.shop_ttcs.model.response.RefreshTokenResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenConvertTo {
    @Autowired
    private ModelMapper modelMapper;

    public RefreshTokenResponse convertTo(RefreshTokenEntity refreshTokenEntity) {
        return modelMapper.map(refreshTokenEntity, RefreshTokenResponse.class);
    }
}

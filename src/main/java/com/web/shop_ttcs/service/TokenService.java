package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.entity.UserEntity;

public interface TokenService {
    public String generateToken(UserEntity userEntity);
    public String generateRefreshToken(UserEntity userEntity);
}

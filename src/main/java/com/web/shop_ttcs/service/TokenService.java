package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.entity.UserEntity;

public interface TokenService {
    public String generateToken(UserEntity userEntity);
    public String generateRefreshToken(UserEntity userEntity);
    public boolean validateToken(String token);
    public UserEntity getUserEntity(String token);
    public boolean validateRefreshToken(String refreshToken);
}

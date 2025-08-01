package com.web.shop_ttcs.service.impl;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.web.shop_ttcs.exception.ex.UserNotFoundException;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.repository.UserRepository;
import com.web.shop_ttcs.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecretKey secretKey;

    @Override
    public String generateToken(UserEntity userEntity) {
        String username = userEntity.getUsername();
        String roleName = userEntity.getRole().getName();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("type", "token")
                .expirationTime(new Date(new Date().getTime() + 1000 * 60 * 60))
                .claim("roles", Collections.singletonList(roleName))
                .build();
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            JWSSigner jwsSigner = new MACSigner(secretKey);
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateRefreshToken(UserEntity userEntity) {
        String username = userEntity.getUsername();
        String roleName = userEntity.getRole().getName();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("type", "refreshToken")
                .expirationTime(new Date(new Date().getTime() + 1000 * 60 * 60 * 7))
                .claim("roles", Collections.singletonList(roleName))
                .build();
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            JWSSigner jwsSigner = new MACSigner(secretKey);
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateToken(String token) {
        try{
            JWSVerifier jwsVerifier = new MACVerifier(secretKey);
            SignedJWT signedJWT = SignedJWT.parse(token);
            if(!signedJWT.verify(jwsVerifier)){
                return false;
            }
            String type = signedJWT.getJWTClaimsSet().getClaim("type").toString();
            if(!type.equals("token")){
                return false;
            }
            if(signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date(new Date().getTime()))){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public UserEntity getUserEntity(String token) {
        if(!validateToken(token)){
            throw new UserNotFoundException("User not found");
        }
        try{
            SignedJWT signedJWT = SignedJWT.parse(token);
            String username = signedJWT.getJWTClaimsSet().getSubject();
            Optional<UserEntity> optional = userRepository.findByUsername(username);
            if(optional.isEmpty()){
                throw new UserNotFoundException("User not found");
            }
            return optional.get();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        try{
            JWSVerifier jwsVerifier = new MACVerifier(secretKey);
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            if(!signedJWT.verify(jwsVerifier)){
                return false;
            }
            String type = signedJWT.getJWTClaimsSet().getClaim("type").toString();
            if(!type.equals("refreshToken")){
                return false;
            }
            if(signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date(new Date().getTime()))){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

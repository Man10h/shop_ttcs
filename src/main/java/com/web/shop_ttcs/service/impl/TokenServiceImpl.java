package com.web.shop_ttcs.service.impl;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.web.shop_ttcs.exception.ex.UserNotFoundException;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.repository.UserRepository;
import com.web.shop_ttcs.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RSAKey rsaKey;

    @Autowired
    private UserRepository userRepository;

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
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.RS256);
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            JWSSigner jwsSigner = new RSASSASigner(rsaKey);
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
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.RS256);
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            JWSSigner jwsSigner = new RSASSASigner(rsaKey);
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateToken(String token) {
        try{
            JWSVerifier jwsVerifier = new RSASSAVerifier(rsaKey.toPublicJWK());
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.verify(jwsVerifier)
                    && !signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date(new Date().getTime()))
                    && !signedJWT.getJWTClaimsSet().getClaim("type").equals("refreshToken");
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public UserEntity getUserEntity(String token) {
        if(validateToken(token)){
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
}

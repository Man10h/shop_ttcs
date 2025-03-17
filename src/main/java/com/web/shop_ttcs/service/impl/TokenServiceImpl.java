package com.web.shop_ttcs.service.impl;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RSAKey rsaKey;

    @Override
    public String generateToken(UserEntity userEntity) {
        String username = userEntity.getUsername();
        String roleName = userEntity.getRole().getName();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
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
}

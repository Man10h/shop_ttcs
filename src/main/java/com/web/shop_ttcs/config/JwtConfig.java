package com.web.shop_ttcs.config;

import com.web.shop_ttcs.model.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Autowired
    private SecretKey secretKey;


    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            return NimbusJwtDecoder.withSecretKey(secretKey).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

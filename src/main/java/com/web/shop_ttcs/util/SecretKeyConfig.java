package com.web.shop_ttcs.util;

import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
public class SecretKeyConfig {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Bean
    public SecretKey secretKey() {
        byte[] secretBytes = Base64.getDecoder().decode(secretKeyString);
        return new OctetSequenceKey.Builder(secretBytes).build().toSecretKey();
    }
}

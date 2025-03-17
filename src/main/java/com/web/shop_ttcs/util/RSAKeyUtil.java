package com.web.shop_ttcs.util;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RSAKeyUtil {

    @Value("${path_private_key}")
    private String pathPrivateKey;

    @Value("${path_public_key}")
    private String pathPublicKey;

    public PrivateKey getPrivateKey() {
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(pathPrivateKey));
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.contains("-----")){
                    sb.append(line);
                }
            }
            br.close();
            byte[] bytes = Base64.getDecoder().decode(sb.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey getPublicKey() {
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(pathPublicKey));
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.contains("-----")){
                    sb.append(line);
                }
            }
            br.close();
            byte[] bytes = Base64.getDecoder().decode(sb.toString());
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RSAKey generateRSAKey() {
        return new RSAKey.Builder((RSAPublicKey) getPublicKey())
                .privateKey(getPrivateKey())
                .build();
    }
}

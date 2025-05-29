package com.web.shop_ttcs.config;

import com.web.shop_ttcs.service.impl.CustomOAuth2UserService;
import com.web.shop_ttcs.util.AuditorAwareImpl;
import com.web.shop_ttcs.util.MyCustomerSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing
public class SecurityFilterChainConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MyCustomerSuccessHandler myCustomerSuccessHandler) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/customer/**").permitAll()
                        .requestMatchers("/home/**").permitAll()
                        .requestMatchers("/manager/**").permitAll()
//                        .requestMatchers("/home/register").permitAll()
//                        .requestMatchers("/home/verify").permitAll()
//                        .requestMatchers("/home/find").permitAll()
//                        .requestMatchers("/home/refreshToken").permitAll()
//                        .requestMatchers("/home/forgotPassword").permitAll()
//                        .requestMatchers("/manager/createShop").permitAll()
//                        .requestMatchers("/manager/deleteShop/{shopId}").permitAll()
//                        .requestMatchers("/manager/editShop").permitAll()
//                        .requestMatchers("/home/infoShop").permitAll()
//                        .requestMatchers("/manager/createProduct").permitAll()
//                        .requestMatchers("/manager/editProduct").permitAll()
//                        .requestMatchers("/manager/deleteProduct/{productId}").permitAll()
//                        .requestMatchers("/customer/follow").permitAll()
//                        .requestMatchers("/customer/unfollow").permitAll()
//                        .requestMatchers("/customer/getAllFollowedShops").permitAll()
//                        .requestMatchers("/customer/createCartItem").permitAll()
//                        .requestMatchers("/customer/getCartItems").permitAll()
//                        .requestMatchers("/customer/deleteCartItem/{cartItemId}").permitAll()
//                        .requestMatchers("/customer/order").permitAll()
//                        .requestMatchers("/customer/cancel").permitAll()
//                        .requestMatchers("/customer/editCartItem").permitAll()
//                        .requestMatchers("/customer/getCartItem").permitAll()
//                        .requestMatchers("/customer/editRating").permitAll()
//                        .requestMatchers("/customer/deleteRating/{ratingId}").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(myCustomerSuccessHandler)
                        .redirectionEndpoint(redirect -> redirect.baseUri("/login/oauth2/code/*"))
                        .authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization"))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                ;
        return http.build();
    }
}

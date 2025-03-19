package com.web.shop_ttcs.util;

import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.repository.UserRepository;
import com.web.shop_ttcs.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class MyCustomerSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");
        Optional<UserEntity> optional = userRepository.findByEmail(email);
        if(optional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserEntity userEntity = optional.get();
        response.getWriter().write(tokenService.generateToken(userEntity));
    }
}

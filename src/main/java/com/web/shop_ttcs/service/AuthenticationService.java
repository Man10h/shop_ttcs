package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.dto.UserLoginDTO;
import com.web.shop_ttcs.model.dto.UserRegisterDTO;
import com.web.shop_ttcs.model.response.UserResponse;

public interface AuthenticationService {
    public String register(UserRegisterDTO userRegisterDTO);
    public String login(UserLoginDTO userLoginDTO);
    public String verify(String email, String verificationCode);
    public String resendVerificationCode(String email);
    public String forgotPassword(String email);
    public UserResponse infoToken(String token);
}

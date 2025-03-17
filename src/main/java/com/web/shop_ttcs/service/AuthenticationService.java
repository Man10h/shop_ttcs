package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.dto.UserLoginDTO;
import com.web.shop_ttcs.model.dto.UserRegisterDTO;

public interface AuthenticationService {
    public String register(UserRegisterDTO userRegisterDTO);
    public String login(UserLoginDTO userLoginDTO);
}

package com.web.shop_ttcs.controller;

import com.web.shop_ttcs.model.dto.ChangePasswordDTO;
import com.web.shop_ttcs.model.dto.UserDTO;
import com.web.shop_ttcs.service.AuthenticationService;
import com.web.shop_ttcs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/editProfile")
    public ResponseEntity<String> editProfile(@ModelAttribute UserDTO userDTO) {
        return ResponseEntity.ok(userService.editProfile(userDTO));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return ResponseEntity.ok(authenticationService.changePassword(changePasswordDTO));
    }
}

package com.web.shop_ttcs.controller;

import com.web.shop_ttcs.model.dto.UserLoginDTO;
import com.web.shop_ttcs.model.dto.UserRegisterDTO;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok("failed to register user");
        }
        return ResponseEntity.ok(authenticationService.register(userRegisterDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok("failed to login");
        }
        return ResponseEntity.ok(authenticationService.login(userLoginDTO));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("verificationCode") String verificationCode,
                                         @RequestParam("email") String email) {
        return ResponseEntity.ok(authenticationService.verify(email, verificationCode));
    }

    @GetMapping("/resend")
    public ResponseEntity<String> resend(@RequestParam("email") String email) {
        return ResponseEntity.ok(authenticationService.resendVerificationCode(email));
    }

}

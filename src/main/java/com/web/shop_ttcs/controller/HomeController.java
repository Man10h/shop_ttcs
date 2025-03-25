package com.web.shop_ttcs.controller;

import com.web.shop_ttcs.model.dto.SearchDTO;
import com.web.shop_ttcs.model.dto.UserLoginDTO;
import com.web.shop_ttcs.model.dto.UserRegisterDTO;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.model.response.ProductResponse;
import com.web.shop_ttcs.model.response.UserResponse;
import com.web.shop_ttcs.service.AuthenticationService;
import com.web.shop_ttcs.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProductService productService;

    @GetMapping("/hello")
    public String home() {
        return "Welcome to shop_ttcs";
    }

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

    @GetMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        return ResponseEntity.ok(authenticationService.forgotPassword(email));
    }

    @GetMapping("/find")
    public ResponseEntity<List<ProductResponse>> find(@ModelAttribute SearchDTO searchDTO) {
        return ResponseEntity.ok(productService.find(searchDTO));
    }

    @GetMapping("/infoToken")
    public ResponseEntity<UserResponse> infoToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(authenticationService.infoToken(token));
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }

    @GetMapping("/product")
    public ResponseEntity<ProductResponse> product(@RequestParam("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/infoProduct")
    public ResponseEntity<ProductResponse> infoProduct(@RequestParam("productId") Long productId) {
        return ResponseEntity.ok(productService.infoProduct(productId));
    }

}

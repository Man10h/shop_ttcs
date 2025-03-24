package com.web.shop_ttcs.controller;

import com.web.shop_ttcs.model.dto.CartItemDTO;
import com.web.shop_ttcs.model.dto.ChangePasswordDTO;
import com.web.shop_ttcs.model.dto.UserDTO;
import com.web.shop_ttcs.model.response.CartItemResponse;
import com.web.shop_ttcs.service.AuthenticationService;
import com.web.shop_ttcs.service.CartItemService;
import com.web.shop_ttcs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/editProfile")
    public ResponseEntity<String> editProfile(@ModelAttribute UserDTO userDTO) {
        return ResponseEntity.ok(userService.editProfile(userDTO));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return ResponseEntity.ok(authenticationService.changePassword(changePasswordDTO));
    }

    @PostMapping("/createCartItem")
    public ResponseEntity<CartItemResponse> createCartItem(@RequestBody CartItemDTO cartItemDTO) {
        return ResponseEntity.ok(cartItemService.createCartItem(cartItemDTO));
    }

    @PostMapping("/editCartItem")
    public ResponseEntity<CartItemResponse> editCartItem(@RequestBody CartItemDTO cartItemDTO) {
        return ResponseEntity.ok(cartItemService.editCartItem(cartItemDTO));
    }

    @GetMapping("/getCartItems")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(cartItemService.getCartItems(userId));
    }

    @GetMapping("/getCartItem")
    public ResponseEntity<CartItemResponse> getCartItem(@RequestParam(name="cartItemId") Long cartItemId) {
        return ResponseEntity.ok(cartItemService.getCartItem(cartItemId));
    }

    @DeleteMapping("/deleteCartItem/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("cartItemId") Long cartItemId) {
        return ResponseEntity.ok(cartItemService.deleteCartItem(cartItemId));
    }



}

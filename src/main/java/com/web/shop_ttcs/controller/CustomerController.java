package com.web.shop_ttcs.controller;

import com.web.shop_ttcs.model.dto.CartItemDTO;
import com.web.shop_ttcs.model.dto.ChangePasswordDTO;
import com.web.shop_ttcs.model.dto.RatingDTO;
import com.web.shop_ttcs.model.dto.UserDTO;
import com.web.shop_ttcs.model.response.CartItemResponse;
import com.web.shop_ttcs.model.response.RatingResponse;
import com.web.shop_ttcs.model.response.ShopResponse;
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
    public ResponseEntity<String> createCartItem(@RequestBody CartItemDTO cartItemDTO) {
        return ResponseEntity.ok(cartItemService.createCartItem(cartItemDTO));
    }

    @PostMapping("/editCartItem")
    public ResponseEntity<String> editCartItem(@RequestBody CartItemDTO cartItemDTO) {
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

    @GetMapping("/order")
    public ResponseEntity<String> order(@RequestParam(name = "cartItemId") Long cartItemId) {
        return ResponseEntity.ok(cartItemService.order(cartItemId));
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel(@RequestParam(name = "cartItemId") Long cartItemId) {
        return ResponseEntity.ok(cartItemService.cancel(cartItemId));
    }

    @GetMapping("/follow")
    public ResponseEntity<String> follow(@RequestParam(name = "userId") Long userId,
                                         @RequestParam(name = "shopId") Long shopId) {
        return ResponseEntity.ok(userService.followShop(userId, shopId));
    }

    @GetMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestParam(name = "userId") Long userId,
                                           @RequestParam(name = "shopId") Long shopId) {
        return ResponseEntity.ok(userService.unfollowShop(userId, shopId));
    }

    @GetMapping("/getAllFollowedShops")
    public ResponseEntity<List<ShopResponse>> getAllFollowedShops(@RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(userService.getAllFollowedShops(userId));
    }

    @PostMapping("/createRating")
    public ResponseEntity<String> createRating(@RequestBody RatingDTO ratingDTO) {
        return ResponseEntity.ok(userService.createRating(ratingDTO));
    }

    @PostMapping("/editRating")
    public ResponseEntity<String> editRating(@RequestBody RatingDTO ratingDTO) {
        return ResponseEntity.ok(userService.editRating(ratingDTO));
    }

    @DeleteMapping("/deleteRating/{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable("ratingId") Long ratingId) {
        return ResponseEntity.ok(userService.deleteRating(ratingId));
    }

    @GetMapping("/ratings")
    public ResponseEntity<List<RatingResponse>> getRatings(@RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(userService.getAllRatings(userId));
    }
}

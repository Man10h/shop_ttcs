package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.dto.CartItemDTO;
import com.web.shop_ttcs.model.response.CartItemResponse;

import java.util.List;

public interface CartItemService {
    public String createCartItem(CartItemDTO cartItemDTO);
    public List<CartItemResponse> getCartItems(Long userId);
    public CartItemResponse getCartItem(Long cartItemId);
    public String deleteCartItem(Long cartItemId);
    public String editCartItem(CartItemDTO cartItemDTO);
    public String order(Long cartItemId);
    public String cancel(Long cartItemId);
}

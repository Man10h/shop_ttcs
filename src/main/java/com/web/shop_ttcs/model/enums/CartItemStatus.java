package com.web.shop_ttcs.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum CartItemStatus {
    ORDERED("Đã đặt hàng"),
    ADDED("Thêm vào giỏ hàng"),
    CANCELLED("Hủy đặt hàng");

    private final String status;
    private CartItemStatus(String status) {
        this.status = status;
    }

    public static Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        for(CartItemStatus cartItemStatus : CartItemStatus.values()){
            map.put(cartItemStatus.toString(), cartItemStatus.status);
        }
        return map;
    }
}

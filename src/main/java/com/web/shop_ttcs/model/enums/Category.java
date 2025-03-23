package com.web.shop_ttcs.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum Category {
    Jean("Jean"),
    Vest("Vest"),
    Hoodie("Hoodie");

    private final String name;
    Category(String name) {
        this.name = name;
    }

    public static Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        for(Category category : Category.values()){
            map.put(category.toString(), category.name);
        }
        return map;
    }
}

package com.web.shop_ttcs.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum Type {
    Electronics("Electronics"),
    Clothing("Clothing"),
    Supermarket("Supermarket"),
    BookStore("BookStore"),
    Sports("Sports");


    private final String name;
    private Type(String name) {
        this.name = name;
    }

    public static Map<String, String> toMap(){
        Map<String, String> map = new HashMap<String, String>();
        for(Type type : Type.values()){
            map.put(type.toString(), type.name);
        }
        return map;
    }
}

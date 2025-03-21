package com.web.shop_ttcs.util;

import org.springframework.stereotype.Component;

public class NumberUtil {
    public static boolean isNumber(String s){
        try{
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

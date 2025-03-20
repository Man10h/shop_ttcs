package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.dto.ShopDTO;
import com.web.shop_ttcs.model.response.ProductResponse;
import com.web.shop_ttcs.model.response.ShopResponse;

import java.util.List;

public interface ShopService {
    public String createShop(ShopDTO shopDTO);
    public String editShop(ShopDTO shopDTO);
    public String deleteShop(Long shopId);
    public ShopResponse infoShop(Long shopId);
}

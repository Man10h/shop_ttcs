package com.web.shop_ttcs.controller;

import com.web.shop_ttcs.model.dto.ShopDTO;
import com.web.shop_ttcs.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/createShop")
    public ResponseEntity<String> createShop(@RequestBody ShopDTO shopDTO) {
        return ResponseEntity.ok(shopService.createShop(shopDTO));
    }

    @PostMapping("/editShop")
    public ResponseEntity<String> editShop(@RequestBody ShopDTO shopDTO) {
        return ResponseEntity.ok(shopService.editShop(shopDTO));
    }

    @DeleteMapping("/deleteShop/{shopId}")
    public ResponseEntity<String> deleteShop(@PathVariable(name = "shopId") Long shopId) {
        return ResponseEntity.ok(shopService.deleteShop(shopId));
    }
}

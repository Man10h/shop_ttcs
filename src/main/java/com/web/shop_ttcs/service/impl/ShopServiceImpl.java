package com.web.shop_ttcs.service.impl;

import com.web.shop_ttcs.exception.ex.ShopNotFoundException;
import com.web.shop_ttcs.exception.ex.UserNotAuthorizedException;
import com.web.shop_ttcs.exception.ex.UserNotFoundException;
import com.web.shop_ttcs.model.dto.ShopDTO;
import com.web.shop_ttcs.model.entity.ShopEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.repository.ShopRepository;
import com.web.shop_ttcs.repository.UserRepository;
import com.web.shop_ttcs.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteShop(Long shopId) {
        Optional<ShopEntity> optionalShop = shopRepository.findById(shopId);
        if (optionalShop.isEmpty()) {
            throw new ShopNotFoundException("Shop not found");
        }
        shopRepository.deleteById(shopId);
        return "deleted shop successfully";
    }

    @Override
    public String createShop(ShopDTO shopDTO) {
        Optional<UserEntity> optionalUser = userRepository.findById(shopDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalUser.get();
        if(!userEntity.getRole().getName().equals("MANAGER")){
            throw new UserNotAuthorizedException("User not authorized");
        }
        if(!userEntity.getShopEntities().isEmpty()){
            return "You cannot create a new shop";
        }
        List<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        ShopEntity shopEntity = ShopEntity.builder()
                .name(shopDTO.getName())
                .address(shopDTO.getAddress())
                .type(shopDTO.getType())
                .productEntities(new ArrayList<>())
                .userEntities(userEntityList)
                .build();
        shopRepository.save(shopEntity);
        return "created a new shop successfully";
    }

    @Override
    public String editShop(ShopDTO shopDTO) {
        if(shopDTO.getShopId() == null){
            return "shop not found";
        }
        Optional<ShopEntity> optionalShop = shopRepository.findById(shopDTO.getShopId());
        if (optionalShop.isEmpty()) {
            throw new ShopNotFoundException("Shop not found");
        }
        ShopEntity shopEntity = optionalShop.get();
        shopEntity.setName(shopDTO.getName());
        shopEntity.setAddress(shopDTO.getAddress());
        shopEntity.setType(shopDTO.getType());
        shopRepository.save(shopEntity);
        return "edited shop successfully";
    }
}

package com.web.shop_ttcs.service.impl;

import com.web.shop_ttcs.converter.CartItemConvertTo;
import com.web.shop_ttcs.exception.ex.CartItemNotFoundException;
import com.web.shop_ttcs.exception.ex.ProductNotFoundException;
import com.web.shop_ttcs.exception.ex.UserNotFoundException;
import com.web.shop_ttcs.model.dto.CartItemDTO;
import com.web.shop_ttcs.model.entity.CartItemEntity;
import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.model.enums.CartItemStatus;
import com.web.shop_ttcs.model.response.CartItemResponse;
import com.web.shop_ttcs.repository.CartItemRepository;
import com.web.shop_ttcs.repository.ProductRepository;
import com.web.shop_ttcs.repository.UserRepository;
import com.web.shop_ttcs.service.CartItemService;
import com.web.shop_ttcs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemConvertTo cartItemConvertTo;

    @Autowired
    private ProductService productService;

    @Override
    public String createCartItem(CartItemDTO cartItemDTO) {
        if(cartItemDTO.getUserId() == null || cartItemDTO.getProductId() == null || cartItemDTO.getQuantity() <= 0){
            return "failed to create cart item";
        }
        Optional<ProductEntity> optionalProduct = productRepository.findById(cartItemDTO.getProductId());
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        Optional<UserEntity> optionalUser = userRepository.findById(cartItemDTO.getUserId());
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalUser.get();
        ProductEntity productEntity = optionalProduct.get();
        if(productEntity.getQuantity() < cartItemDTO.getQuantity()){
            return "failed to create cart item";
        }
        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .userEntity(userEntity)
                .productEntity(productEntity)
                .status("ADDED")
                .quantity(cartItemDTO.getQuantity())
                .build();
        cartItemRepository.save(cartItemEntity);
        productEntity.setQuantity(productEntity.getQuantity() - cartItemDTO.getQuantity());
        productRepository.save(productEntity);
        return "create cart item successfully";
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Long userId) {
        List<CartItemEntity> cartItemEntities = cartItemRepository.findByUserEntity_Id(userId);
        if(cartItemEntities.isEmpty()){
            return null;
        }
        return cartItemEntities.stream()
                .map(it -> cartItemConvertTo.convertTo(it))
                .toList();
    }

    @Override
    public CartItemResponse getCartItem(Long cartItemId) {
        Optional<CartItemEntity> optional = cartItemRepository.findById(cartItemId);
        return optional.map(cartItemEntity -> cartItemConvertTo.convertTo(cartItemEntity)).orElse(null);
    }

    @Transactional
    public String deleteCartItem(Long cartItemId) {
        Optional<CartItemEntity> optional = cartItemRepository.findById(cartItemId);
        if(optional.isEmpty()){
            return "failed to delete";
        }
        CartItemEntity cartItemEntity = optional.get();
        ProductEntity productEntity = productRepository.findByCartItemId(cartItemId);
        productEntity.setQuantity(productEntity.getQuantity() + cartItemEntity.getQuantity());
        productRepository.save(productEntity);
        cartItemRepository.delete(cartItemEntity);
        return "deleted successfully";
    }

    @Override
    public String editCartItem(CartItemDTO cartItemDTO) {
        if(cartItemDTO.getId()==null || cartItemDTO.getProductId() == null
                || cartItemDTO.getUserId() == null || cartItemDTO.getQuantity() == null){
            return "failed to edit cart item";
        }
        Optional<CartItemEntity> optional = cartItemRepository.findById(cartItemDTO.getId());
        if(optional.isEmpty()){
            throw new CartItemNotFoundException("Cart item not found");
        }
        Optional<ProductEntity> optionalProduct = productRepository.findById(cartItemDTO.getProductId());
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        ProductEntity productEntity = optionalProduct.get();
        if(productEntity.getQuantity() < cartItemDTO.getQuantity()){
            return "failed to edit cart item";
        }
        CartItemEntity cartItemEntity = optional.get();
        Long quantityOld = cartItemEntity.getQuantity();
        cartItemEntity.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(cartItemEntity);

        productEntity.setQuantity(productEntity.getQuantity() + quantityOld - cartItemDTO.getQuantity());
        productRepository.save(productEntity);

        return "edit cart item successfully";
    }

    @Override
    public String order(Long cartItemId) {
        Optional<CartItemEntity> optional = cartItemRepository.findById(cartItemId);
        if(optional.isEmpty()){
            throw new CartItemNotFoundException("Cart item not found");
        }
        CartItemEntity cartItemEntity = optional.get();
        if(!cartItemEntity.getStatus().equals("ADDED")){
            return "failed to order cart item";
        }
        cartItemEntity.setStatus("ORDERED");
        cartItemEntity.setOrderDate(new Date(new Date().getTime()));
        cartItemEntity.setDeliveryDate(new Date(new Date().getTime() + 3 * 24 * 60 * 60 * 1000));
        cartItemRepository.save(cartItemEntity);
        return "order cart item successfully";
    }

    @Override
    public String cancel(Long cartItemId) {
        Optional<CartItemEntity> optional = cartItemRepository.findById(cartItemId);
        if(optional.isEmpty()){
            throw new CartItemNotFoundException("Cart item not found");
        }
        CartItemEntity cartItemEntity = optional.get();
        if(!cartItemEntity.getStatus().equals("ORDERED")){
            return "failed to cancel cart item";
        }
        cartItemEntity.setStatus("CANCELLED");
        cartItemEntity.setOrderDate(null);
        cartItemEntity.setDeliveryDate(null);
        cartItemRepository.save(cartItemEntity);
        return "cancel cart item successfully";
    }


}

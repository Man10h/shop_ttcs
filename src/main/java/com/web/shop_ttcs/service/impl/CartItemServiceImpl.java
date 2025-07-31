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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemConvertTo cartItemConvertTo;
    private final ProductService productService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static Long randomTtl(){
        return 5 + new Random().nextLong(5);
    }


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
        //Lua script
        String keyProduct = "product:" + productEntity.getId() + ":stock";
        Long stock = (Long) redisTemplate.opsForValue().get(keyProduct);
        if(stock == null){
            redisTemplate.opsForValue().set(keyProduct, productEntity.getQuantity(), Duration.ofMinutes(randomTtl()));
        }

        String luaScript =
                "local stock = tonumber(redis.call('GET', KEYS[1]))\n" +
                        "local quantity = tonumber(ARGV[1])\n" +
                        "if stock >= quantity then\n" +
                        "    redis.call('DECRBY', KEYS[1], quantity)\n" +
                        "    return 1\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Long.class);

        Long result = redisTemplate.execute(
                redisScript, Collections.singletonList(keyProduct), cartItemDTO.getQuantity()
        );
        if(result == 0) {
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

        String key = "user:" + cartItemDTO.getId() + ":cart";
        List<CartItemResponse> cartItemResponses = (List<CartItemResponse>) redisTemplate.opsForValue().get(key);
        if(cartItemResponses != null){
            redisTemplate.delete(key);
        }
        return "create cart item successfully";
    }

    //strategy: cache aside
    @Override
    public List<CartItemResponse> getCartItems(Long userId) {
        String key = "user:" + userId + ":cart";
        List<CartItemResponse> cartItemResponsesRedisCache = (List<CartItemResponse>) redisTemplate.opsForValue().get(key);
        if(cartItemResponsesRedisCache != null){
            return cartItemResponsesRedisCache;
        }
        List<CartItemEntity> cartItemEntities = cartItemRepository.findByUserEntity_Id(userId);
        if (cartItemEntities.isEmpty()) {
            return List.of();
        }
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (CartItemEntity cartItemEntity : cartItemEntities) {
            cartItemResponses.add(cartItemConvertTo.convertTo(cartItemEntity));
        }
        redisTemplate.opsForValue().set(key, cartItemResponses, Duration.ofMinutes(randomTtl()));
        return cartItemResponses;
    }

    @Override
    public CartItemResponse getCartItem(Long cartItemId) {
        Optional<CartItemEntity> optional = cartItemRepository.findById(cartItemId);
        return optional.map(cartItemConvertTo::convertTo).orElse(null);
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
        String key = "user:" + cartItemEntity.getUserEntity().getId() + ":cart";
        List<CartItemResponse> cartItemResponses = (List<CartItemResponse>) redisTemplate.opsForValue().get(key);
        if(cartItemResponses != null){
            redisTemplate.delete(key);
        }
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
        String key = "user:" + cartItemDTO.getId() + ":cart";
        List<CartItemResponse> cartItemResponses = (List<CartItemResponse>) redisTemplate.opsForValue().get(key);
        if(cartItemResponses != null){
            redisTemplate.delete(key);
        }
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

        String key = "user:" + cartItemEntity.getUserEntity().getId() + ":cart";
        List<CartItemResponse> cartItemResponses = (List<CartItemResponse>) redisTemplate.opsForValue().get(key);
        if(cartItemResponses != null){
            redisTemplate.delete(key);
        }
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

        String key = "user:" + cartItemEntity.getUserEntity().getId() + ":cart";
        List<CartItemResponse> cartItemResponses = (List<CartItemResponse>) redisTemplate.opsForValue().get(key);
        if(cartItemResponses != null){
            redisTemplate.delete(key);
        }
        return "cancel cart item successfully";
    }


}

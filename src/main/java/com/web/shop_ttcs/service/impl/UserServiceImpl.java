package com.web.shop_ttcs.service.impl;

import com.cloudinary.Cloudinary;
import com.web.shop_ttcs.converter.RatingConvertTo;
import com.web.shop_ttcs.converter.ShopConvertTo;
import com.web.shop_ttcs.exception.ex.ProductNotFoundException;
import com.web.shop_ttcs.exception.ex.RatingNotFoundException;
import com.web.shop_ttcs.exception.ex.ShopNotFoundException;
import com.web.shop_ttcs.exception.ex.UserNotFoundException;
import com.web.shop_ttcs.model.dto.RatingDTO;
import com.web.shop_ttcs.model.dto.UserDTO;
import com.web.shop_ttcs.model.entity.*;
import com.web.shop_ttcs.model.response.RatingResponse;
import com.web.shop_ttcs.model.response.ShopResponse;
import com.web.shop_ttcs.repository.*;
import com.web.shop_ttcs.service.CloudinaryService;
import com.web.shop_ttcs.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopConvertTo shopConvertTo;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingConvertTo ratingConvertTo;


    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class})
    public String editProfile(UserDTO userDTO) {
        if(userDTO.getId() == null){
            return "cannot edit profile";
        }
        Optional<UserEntity> optional = userRepository.findById(userDTO.getId());
        if(optional.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optional.get();
        if(userDTO.getPhoneNumber() != null){
            userEntity.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if(userDTO.getFirstName() != null){
            userEntity.setFirstName(userDTO.getFirstName());
        }
        if(userDTO.getLastName() != null){
            userEntity.setLastName(userDTO.getLastName());
        }
        if(userDTO.getAge() != null){
            userEntity.setAge(userDTO.getAge());
        }
        if(userDTO.getSex() != null){
            userEntity.setSex(userDTO.getSex());
        }
        if(userDTO.getImages() != null && !userDTO.getImages().isEmpty()){
            imageRepository.deleteByUserEntity(userEntity);
            for(MultipartFile multipartFile : userDTO.getImages()){
                Map<String, Object> result = cloudinaryService.upload(multipartFile);
                ImageEntity imageEntity = ImageEntity.builder()
                        .userEntity(userEntity)
                        .url((String) result.get("url"))
                        .publicId((String) result.get("public_id"))
                        .name((String) result.get("original_filename"))
                        .build();
                imageRepository.save(imageEntity);
            }
        }
        userRepository.save(userEntity);
        return "edit profile successfully";
    }

    @Override
    public String followShop(Long userId, Long shopId) {
        if(userId == null || shopId == null){
            return "cannot follow shop";
        }
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalUser.get();
        Optional<ShopEntity> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            throw new ShopNotFoundException("Shop not found");
        }
        ShopEntity shopEntity = optionalShop.get();
        userEntity.getShopEntities().add(shopEntity);
        userRepository.save(userEntity);
        return "follow shop successfully";
    }

    @Transactional(readOnly = true)
    public List<ShopResponse> getAllFollowedShops(Long userId) {
        if(userId == null){
            return null;
        }
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalUser.get();
        return userEntity.getShopEntities().stream()
                .map(it -> shopConvertTo.convertTo(it))
                .collect(Collectors.toList());
    }

    @Override
    public String unfollowShop(Long userId, Long shopId) {
        if(userId == null || shopId == null){
            return "cannot unfollow shop";
        }
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalUser.get();
        Optional<ShopEntity> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            throw new ShopNotFoundException("Shop not found");
        }
        ShopEntity shopEntity = optionalShop.get();
        if(!userEntity.getShopEntities().contains(shopEntity)){
            return "you not followed shop";
        }
        userEntity.getShopEntities().remove(shopEntity);
        userRepository.save(userEntity);
        return "unfollow shop successfully";
    }

    @Override
    public String createRating(RatingDTO ratingDTO) {
        if(ratingDTO.getUserId() == null || ratingDTO.getProductId() == null
        || ratingDTO.getRate() == null){
            return "cannot rate product";
        }
        Optional<UserEntity> optionalUser = userRepository.findById(ratingDTO.getUserId());
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalUser.get();
        Optional<ProductEntity> optionalProduct = productRepository.findById(ratingDTO.getProductId());
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        ProductEntity productEntity = optionalProduct.get();
        // check rating in database
        Optional<RatingEntity> optionalRating = ratingRepository.findByUserEntityAndProductEntity(userEntity, productEntity);
        if(optionalRating.isPresent()){
            return "you already have this rating";
        }
        // create new rating
        RatingEntity ratingEntity = RatingEntity.builder()
                .productEntity(productEntity)
                .userEntity(userEntity)
                .content(ratingDTO.getContent())
                .build();
        ratingRepository.save(ratingEntity);
        // update product
        Long numberOfRate = productEntity.getNumberOfRate() + 1;
        Double totalOfRate = productEntity.getTotalOfRate() + numberOfRate;
        productEntity.setNumberOfRate(numberOfRate);
        productEntity.setTotalOfRate(totalOfRate);
        productEntity.setRating(totalOfRate/numberOfRate);
        productRepository.save(productEntity);
        return "rating product successfully";
    }

    @Override
    public String editRating(RatingDTO ratingDTO) {
        if(ratingDTO.getId() == null){
            return "cannot edit rating";
        }
        Optional<RatingEntity> optionalRating = ratingRepository.findById(ratingDTO.getId());
        if(optionalRating.isEmpty()){
            throw new RatingNotFoundException("Rating not found");
        }
        RatingEntity ratingEntity = optionalRating.get();
        if(!ratingDTO.getContent().isEmpty()){
            ratingEntity.setContent(ratingDTO.getContent());
        }
        if(ratingDTO.getRate() != null){
            ratingEntity.setRating(ratingDTO.getRate());
        }
        ratingRepository.save(ratingEntity);
        return "edit rating product successfully";
    }

    @Override
    public List<RatingResponse> getAllRatings(Long userId) {
        if(userId == null){
            return null;
        }
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalUser.get();
        List<RatingEntity> ratingEntities = ratingRepository.findByUserEntity(userEntity);
        if(ratingEntities.isEmpty()){
            return null;
        }
        return ratingEntities.stream()
                .map(it -> ratingConvertTo.convertToRating(it))
                .collect(Collectors.toList());
    }

    @Override
    public String deleteRating(Long ratingId) {
        Optional<RatingEntity> optionalRating = ratingRepository.findById(ratingId);
        if(optionalRating.isEmpty()){
            throw new RatingNotFoundException("Rating not found");
        }
        RatingEntity ratingEntity = optionalRating.get();
        ratingRepository.delete(ratingEntity);
        return "delete rating product successfully";
    }

}

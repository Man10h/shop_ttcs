package com.web.shop_ttcs.service.impl;

import com.cloudinary.Cloudinary;
import com.web.shop_ttcs.exception.ex.UserNotFoundException;
import com.web.shop_ttcs.model.dto.UserDTO;
import com.web.shop_ttcs.model.entity.ImageEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.repository.ImageRepository;
import com.web.shop_ttcs.repository.UserRepository;
import com.web.shop_ttcs.service.CloudinaryService;
import com.web.shop_ttcs.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


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
        return "edit profile successful";
    }
}

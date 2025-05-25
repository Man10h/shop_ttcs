package com.web.shop_ttcs.service.impl;

import com.web.shop_ttcs.converter.ProductConvertTo;
import com.web.shop_ttcs.converter.RatingConvertTo;
import com.web.shop_ttcs.exception.ex.ProductNotFoundException;
import com.web.shop_ttcs.exception.ex.ShopNotFoundException;
import com.web.shop_ttcs.model.dto.ProductDTO;
import com.web.shop_ttcs.model.dto.SearchDTO;
import com.web.shop_ttcs.model.entity.ImageEntity;
import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.RatingEntity;
import com.web.shop_ttcs.model.entity.ShopEntity;
import com.web.shop_ttcs.model.response.ProductResponse;
import com.web.shop_ttcs.model.response.RatingResponse;
import com.web.shop_ttcs.repository.ImageRepository;
import com.web.shop_ttcs.repository.ProductRepository;
import com.web.shop_ttcs.repository.ShopRepository;
import com.web.shop_ttcs.service.CloudinaryService;
import com.web.shop_ttcs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductConvertTo productConvertTo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private RatingConvertTo ratingConvertTo;


    @Transactional(readOnly = true)
    @Cacheable(value = "product", key = "#searchDTO.toString()")
    public List<ProductResponse> find(SearchDTO searchDTO) {
        List<ProductEntity> productEntities = productRepository.find(searchDTO);
        if(productEntities.isEmpty()){
            return null;
        }
        return productEntities.stream()
                .map(it -> productConvertTo.convertTo(it))
                .collect(Collectors.toList());
    }

    @Override
    public String createProduct(ProductDTO productDTO) {
        if(productDTO.getShopId() == null) {
            return "cannot create a product without shop";
        }
        Optional<ShopEntity> optionalShop = shopRepository.findById(productDTO.getShopId());
        if(optionalShop.isEmpty()) {
            throw new ShopNotFoundException("Shop not found");
        }
        ShopEntity shopEntity = optionalShop.get();
        ProductEntity productEntity = ProductEntity.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .category(productDTO.getCategory())
                .quantity(productDTO.getQuantity())
                .numberOfRate(1L)
                .totalOfRate(5.0)
                .rating(5.0)
                .cartItemEntities(new ArrayList<>())
                .imageEntities(new ArrayList<>())
                .shopEntity(shopEntity)
                .ratingEntities(new ArrayList<>())
                .build();
        productRepository.save(productEntity);

        if(!productDTO.getImages().isEmpty()){
            for(MultipartFile file : productDTO.getImages()) {
                Map<String, Object> results = cloudinaryService.upload(file);
                ImageEntity imageEntity = ImageEntity.builder()
                        .url(results.get("url").toString())
                        .name(results.get("original_filename").toString())
                        .publicId(results.get("public_id").toString())
                        .productEntity(productEntity)
                        .build();
                imageRepository.save(imageEntity);
            }
        }

        return "create product successfully";
    }

    @Override
    public String editProduct(ProductDTO productDTO) {
        if(productDTO.getProductId() == null) {
            return "can't find product";
        }
        Optional<ProductEntity> optionalProduct = productRepository.findById(productDTO.getProductId());
        if(optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }
        ProductEntity productEntity = optionalProduct.get();
        if(!productDTO.getName().isBlank()){
            productEntity.setName(productDTO.getName());
        }
        if(!productDTO.getDescription().isBlank()){
            productEntity.setDescription(productDTO.getDescription());
        }
        if(productDTO.getPrice() != null){
            productEntity.setPrice(productDTO.getPrice());
        }
        if(!productDTO.getCategory().isBlank()){
            productEntity.setCategory(productDTO.getCategory());
        }
        if (productDTO.getQuantity() != null){
            productEntity.setQuantity(productDTO.getQuantity());
        }

        if(!productDTO.getImages().isEmpty()){
            for(MultipartFile file : productDTO.getImages()) {
                Map<String, Object> results = cloudinaryService.upload(file);
                ImageEntity imageEntity = ImageEntity.builder()
                        .url(results.get("url").toString())
                        .name(results.get("original_filename").toString())
                        .publicId(results.get("public_id").toString())
                        .productEntity(productEntity)
                        .build();
                imageRepository.save(imageEntity);
            }
        }

        productRepository.save(productEntity);
        return "edit product successfully";
    }

    @Override
    public String deleteProduct(Long productId) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }
        productRepository.delete(optionalProduct.get());
        return "delete product successfully";
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "product", key = "#productId")
    public ProductResponse getProductById(Long productId) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }
        ProductEntity productEntity = optionalProduct.get();
        return productConvertTo.convertTo(productEntity);
    }
}

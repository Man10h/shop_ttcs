package com.web.shop_ttcs.service.impl;

import com.web.shop_ttcs.converter.RatingConvertTo;
import com.web.shop_ttcs.exception.ex.ProductNotFoundException;
import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.RatingEntity;
import com.web.shop_ttcs.model.response.RatingResponse;
import com.web.shop_ttcs.repository.ProductRepository;
import com.web.shop_ttcs.repository.RatingRepository;
import com.web.shop_ttcs.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private RatingConvertTo ratingConvertTo;

    public RatingServiceImpl(ProductRepository productRepository) {
    }

    @Override
    public List<RatingResponse> getRatings(Long productId) {
        if (productId == null) {
            return null;
        }
        List<RatingEntity> ratingEntities = ratingRepository.findByProductEntity_Id(productId);
        if(ratingEntities == null) {
            return null;
        }
        return ratingEntities.stream()
                .map(it -> ratingConvertTo.convertToComment(it))
                .collect(Collectors.toList());
    }
}

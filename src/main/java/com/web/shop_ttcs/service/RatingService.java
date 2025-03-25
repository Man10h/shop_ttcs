package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.response.RatingResponse;

import java.util.List;

public interface RatingService {
    public List<RatingResponse> getRatings(Long productId);
}

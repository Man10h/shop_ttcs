package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.dto.RatingDTO;
import com.web.shop_ttcs.model.dto.UserDTO;
import com.web.shop_ttcs.model.response.RatingResponse;
import com.web.shop_ttcs.model.response.ShopResponse;

import java.util.List;

public interface UserService {
    public String editProfile(UserDTO userDTO);
    public String followShop(Long userId, Long shopId);
    public List<ShopResponse> getAllFollowedShops(Long userId);
    public String unfollowShop(Long userId, Long shopId);

    public String createRating(RatingDTO ratingDTO);
    public String editRating(RatingDTO ratingDTO);
    public List<RatingResponse> getAllRatings(Long userId);
    public String deleteRating(Long ratingId);
}

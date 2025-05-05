package com.web.shop_ttcs.model.response;

import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingResponse {
    private Long id;
    private String content;
    private Double rating;
    private UserRatingResponse userRatingResponse;
    private ProductRatingResponse productRatingResponse;
}

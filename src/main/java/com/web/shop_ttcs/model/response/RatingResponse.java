package com.web.shop_ttcs.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingResponse implements Serializable {
    private Long id;
    private String content;
    private Double rating;
    private UserRatingResponse userRatingResponse;
    private ProductRatingResponse productRatingResponse;
}

package com.web.shop_ttcs.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Long quantity;
    private Double rating;
    private List<ImageResponse> imageResponses;
    private List<RatingResponse> ratingResponses;
    private Long shopId;
}

package com.web.shop_ttcs.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private Double rating;
    private String content;
}

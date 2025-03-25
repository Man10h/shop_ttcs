package com.web.shop_ttcs.model.response;

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
    private UserResponse userResponse;
    private ProductResponse productResponse;
}

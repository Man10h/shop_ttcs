package com.web.shop_ttcs.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Long quantity;
    private Double rating;
    private List<ImageResponse> imageResponses;
    private Long shopId;
}

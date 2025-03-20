package com.web.shop_ttcs.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private Long shopId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Long quantity;
}

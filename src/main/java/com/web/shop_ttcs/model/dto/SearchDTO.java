package com.web.shop_ttcs.model.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchDTO {
    private String name;
    private String description;
    private Double price;
    private String category;
    private Long quantity;
    private Double rating;
    private String shopAddress;
    private String shopName;
    private Double shopRating;
    private String shopType;
}

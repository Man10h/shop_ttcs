package com.web.shop_ttcs.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponse {
    private Long id;
    private String name;
    private String type;
    private Double rating;
    private Long followers;
    private List<ProductResponse> productResponses;
}

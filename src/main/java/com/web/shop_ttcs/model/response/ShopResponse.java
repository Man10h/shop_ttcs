package com.web.shop_ttcs.model.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponse implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String type;
    private Double rating;
    private Long followers;
    private List<ProductResponse> productResponses;
}

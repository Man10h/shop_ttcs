package com.web.shop_ttcs.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {
    private Long userId;
    private Long shopId;
    private String name;
    private String address;
    private String type;
}

package com.web.shop_ttcs.model.dto;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long quantity;
    private Long userId;
    private Long productId;
}

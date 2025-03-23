package com.web.shop_ttcs.model.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Long id;
    private Date orderDate;
    private Date deliveryDate;
    private Long quantity;
    private String status;
    private ProductResponse productResponse;
}

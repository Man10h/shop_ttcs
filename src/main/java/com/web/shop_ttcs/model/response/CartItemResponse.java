package com.web.shop_ttcs.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse implements Serializable {
    private Long id;
    private Date orderDate;
    private Date deliveryDate;
    private Long quantity;
    private String status;
    private ProductResponse productResponse;
}

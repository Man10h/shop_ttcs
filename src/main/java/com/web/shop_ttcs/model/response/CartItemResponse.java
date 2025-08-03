package com.web.shop_ttcs.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse implements Serializable {
    private Long id;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private Long quantity;
    private String status;
    private ProductResponse productResponse;
}

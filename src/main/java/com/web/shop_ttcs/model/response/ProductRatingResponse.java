package com.web.shop_ttcs.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRatingResponse implements Serializable {
    private Long id;
    private String name;
    private String url;
}

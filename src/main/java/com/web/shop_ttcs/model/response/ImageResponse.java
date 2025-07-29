package com.web.shop_ttcs.model.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse implements Serializable {
    private Long id;
    private String url;
}

package com.web.shop_ttcs.model.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse implements Serializable {
    private Long id;
    private String refreshToken;
}

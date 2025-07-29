package com.web.shop_ttcs.model.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Long age;
    private String sex;
    private String phoneNumber;
    private String email;
    private Long coins;
    private Boolean enabled;
    private String roleName;
    private List<ImageResponse> images;
    private List<RefreshTokenResponse> refreshTokens;
    private List<Long> shops;
}

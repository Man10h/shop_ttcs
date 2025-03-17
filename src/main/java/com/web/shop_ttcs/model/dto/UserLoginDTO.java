package com.web.shop_ttcs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @JsonProperty("username")
    @NotBlank(message = "required")
    private String username;

    @JsonProperty("password")
    @NotBlank(message = "required")
    private String password;
}

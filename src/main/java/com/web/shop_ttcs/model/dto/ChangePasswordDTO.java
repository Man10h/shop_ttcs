package com.web.shop_ttcs.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
    private Long userId;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}

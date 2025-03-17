package com.web.shop_ttcs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @JsonProperty("username")
    @NotBlank(message = "required")
    private String username;

    @JsonProperty("firstName")
    @NotBlank(message = "required")
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank(message = "required")
    private String lastName;

    @JsonProperty("password")
    @NotBlank(message = "required")
    private String password;

    @JsonProperty("confirmPassword")
    @NotBlank(message = "required")
    private String confirmPassword;

    @JsonProperty("age")
    @NotNull(message = "required")
    private Long age;

    @JsonProperty("sex")
    @NotBlank(message = "required")
    private String sex;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "required")
    private String phoneNumber;

    @JsonProperty("email")
    @NotBlank(message = "required")
    private String email;

    @JsonProperty("roleId")
    @NotNull(message = "required")
    private Long roleId;
}

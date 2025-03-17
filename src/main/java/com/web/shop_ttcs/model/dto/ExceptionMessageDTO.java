package com.web.shop_ttcs.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessageDTO {
    private Long status;
    private String message;
}

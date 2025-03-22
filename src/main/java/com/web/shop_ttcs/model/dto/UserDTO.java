package com.web.shop_ttcs.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Long age;
    private String sex;
    private String phoneNumber;
    private List<MultipartFile> images;
}

package com.web.shop_ttcs.model.dto;

import jakarta.mail.Multipart;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private Long shopId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Long quantity;
    private List<MultipartFile> images;
}

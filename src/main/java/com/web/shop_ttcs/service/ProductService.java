package com.web.shop_ttcs.service;

import com.web.shop_ttcs.model.dto.ProductDTO;
import com.web.shop_ttcs.model.dto.SearchDTO;
import com.web.shop_ttcs.model.response.ProductResponse;

import java.util.List;

public interface ProductService {
    public List<ProductResponse> find(SearchDTO searchDTO);
    public String createProduct(ProductDTO productDTO);
    public String editProduct(ProductDTO productDTO);
    public String deleteProduct(Long productId);
    public ProductResponse getProductById(Long productId);
}

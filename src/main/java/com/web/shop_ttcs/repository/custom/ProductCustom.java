package com.web.shop_ttcs.repository.custom;

import com.web.shop_ttcs.model.dto.ProductDTO;
import com.web.shop_ttcs.model.dto.SearchDTO;
import com.web.shop_ttcs.model.entity.ProductEntity;

import java.util.List;

public interface ProductCustom {
    public List<ProductEntity> find(SearchDTO searchDTO);
}

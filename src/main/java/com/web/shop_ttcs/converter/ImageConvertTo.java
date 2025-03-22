package com.web.shop_ttcs.converter;

import com.web.shop_ttcs.model.entity.ImageEntity;
import com.web.shop_ttcs.model.response.ImageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageConvertTo {
    @Autowired
    private ModelMapper modelMapper;

    public ImageResponse convertTo(ImageEntity imageEntity) {
        return modelMapper.map(imageEntity, ImageResponse.class);
    }
}

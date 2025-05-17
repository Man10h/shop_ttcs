package com.web.shop_ttcs.repository.custom.impl;

import com.web.shop_ttcs.model.dto.ProductDTO;
import com.web.shop_ttcs.model.dto.SearchDTO;
import com.web.shop_ttcs.model.entity.ProductEntity;
import com.web.shop_ttcs.repository.custom.ProductCustom;
import com.web.shop_ttcs.util.NumberUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.List;

public class ProductCustomImpl implements ProductCustom {

    @PersistenceContext
    private EntityManager em;

    public static void joinTable(SearchDTO searchDTO, StringBuilder sql) {
        if(!searchDTO.getShopName().isEmpty() || !searchDTO.getShopType().isEmpty()
        || !searchDTO.getShopAddress().isEmpty() || searchDTO.getShopRating() != null) {
            sql.append("JOIN shop s ON s.id = p.shop_id ");
        }
    }

    public static void queryNormal(SearchDTO searchDTO, StringBuilder sql) {
        try{
            Field[] fields = SearchDTO.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if(!fieldName.equals("shopRating") && !fieldName.equals("rating")
                        && !fieldName.equals("price") && !fieldName.equals("shopName")
                        && !fieldName.equals("shopAddress") && !fieldName.equals("shopType")) {
                    Object value =  field.get(searchDTO);
                    if(value != null) {
                        if(NumberUtil.isNumber(value.toString())) {
                            sql.append(" AND p." + fieldName + " = " + value.toString() + " ");
                        }
                        else{
                            if(!value.toString().equals("")) {
                                sql.append(" AND p." + fieldName + " LIKE '%" + value.toString() + "%' ");
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void querySpecial(SearchDTO searchDTO, StringBuilder sql) {
        if(!searchDTO.getShopName().isEmpty()){
            sql.append(" AND s.name LIKE '%" + searchDTO.getShopName() + "%' ");
        }
        if(!searchDTO.getShopType().isEmpty()){
            sql.append(" AND s.type LIKE '%" + searchDTO.getShopType() + "%' ");
        }
        if(!searchDTO.getShopAddress().isEmpty()){
            sql.append(" AND s.address LIKE '%" + searchDTO.getShopAddress() + "%' ");
        }
        if(searchDTO.getShopRating() != null){
            double maxRating = searchDTO.getShopRating() + 0.5;
            double minRating = searchDTO.getRating() > 0.5 ? searchDTO.getRating() - 0.5 : 0.0;
            sql.append(" AND s.rating >= " + minRating + " AND + s.rating <= " + maxRating + " ");
        }
        if(searchDTO.getRating() != null){
            double maxRating = searchDTO.getRating() + 0.5;
            double minRating = searchDTO.getRating() > 0.5 ? searchDTO.getRating() - 0.5 : 0.0;
            sql.append(" AND p.rating >= " + minRating + " AND p.rating <= " + maxRating + " ");
        }
        if(searchDTO.getPrice() != null){
            double maxPrice = searchDTO.getPrice() + 5000.0;
            double minPrice = searchDTO.getPrice() > 5000 ? searchDTO.getPrice() - 5000 : 0.0;
            sql.append(" AND p.price >= " + minPrice + " AND p.price <= " + maxPrice + " ");
        }
    }

    @Override
    public List<ProductEntity> find(SearchDTO searchDTO) {
        StringBuilder sql = new StringBuilder("SELECT p.* FROM product p ");
        joinTable(searchDTO, sql);
        sql.append(" WHERE 1 = 1 ");
        queryNormal(searchDTO, sql);
        querySpecial(searchDTO, sql);
        Query query = em.createNativeQuery(sql.toString(), ProductEntity.class);
        return query.getResultList();
    }
}

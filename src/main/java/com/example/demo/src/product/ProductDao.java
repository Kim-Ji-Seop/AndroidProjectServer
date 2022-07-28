package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductRes> getProducts(int divId) {
        String getProductQuery =
                "select PRODUCT_IMAGE,BRAND_NAME,PRODUCT_NAME,PRODUCT_PRICE\n" +
                "from PRODUCT inner join BRAND\n" +
                "on PRODUCT.BRAND_ID = BRAND.BRAND_ID and DIV_ID = ?;";

        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getString("PRODUCT_IMAGE"),
                        rs.getString("BRAND_NAME"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getInt("PRODUCT_PRICE")),
                divId);
    }
    public List<GetProductRes> getProductsWithCategory(int divId,Integer categoryId) {
        String getProductQuery =
                        "select PRODUCT_IMAGE,BRAND_NAME,PRODUCT_NAME,PRODUCT_PRICE\n" +
                        "from PRODUCT inner join BRAND\n" +
                        "on PRODUCT.BRAND_ID = BRAND.BRAND_ID and DIV_ID = ? and CATEGORY_ID = ?;";

        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getString("PRODUCT_IMAGE"),
                        rs.getString("BRAND_NAME"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getInt("PRODUCT_PRICE")),
                divId,categoryId);
    }
    public GetGoodsRes getGoods(int productId){
        String getGoodsQuery =
                "select BRAND_NAME, PRODUCT_IMAGE,PRODUCT_NAME,PRODUCT_INFO,PRODUCT_PRICE\n" +
                "from PRODUCT\n" +
                "inner join BRAND\n" +
                "on PRODUCT.BRAND_ID = BRAND.BRAND_ID and PRODUCT_ID=?;";
        return this.jdbcTemplate.queryForObject(getGoodsQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getString("BRAND_NAME"),
                        rs.getString("PRODUCT_IMAGE"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getString("PRODUCT_INFO"),
                        rs.getInt("PRODUCT_PRICE")),
                productId);
    }

    public List<GetSizeRes> getSize(int productId){
        String getSizeQuery;
        int divId = this.jdbcTemplate.queryForObject(
                "select DIV_ID from PRODUCT where PRODUCT_ID = ?;",
                int.class, productId);
        if(divId == 1 || divId == 2){
            getSizeQuery =
                    "select SIZE_TYPE, TOP_HEIGHT, SHOULDER, CHEST, SLEEVE_LENGTH\n" +
                            "from PRODUCT_SIZE, PRODUCT\n" +
                            "where PRODUCT.PRODUCT_ID=? and PRODUCT.DIV_ID = PRODUCT_SIZE.DIV_ID;";
            return this.jdbcTemplate.query(getSizeQuery,
                    (rs, rowNum) -> new GetTopSizeRes(
                            rs.getString("SIZE_TYPE"),
                            rs.getInt("TOP_HEIGHT"),
                            rs.getInt("SHOULDER"),
                            rs.getInt("CHEST"),
                            rs.getInt("SLEEVE_LENGTH")),
                    productId);
        }else if(divId == 3){
            getSizeQuery =
                    "select SIZE_TYPE, PANTS_HEIGHT, WAIST, HIP, THIGH, HEM, RISE\n" +
                            "from PRODUCT_SIZE, PRODUCT\n" +
                            "where PRODUCT.PRODUCT_ID=? and PRODUCT.DIV_ID = PRODUCT_SIZE.DIV_ID;";
            return this.jdbcTemplate.query(getSizeQuery,
                    (rs, rowNum) -> new GetPantsSizeRes(
                            rs.getString("SIZE_TYPE"),
                            rs.getInt("PANTS_HEIGHT"),
                            rs.getInt("WAIST"),
                            rs.getInt("HIP"),
                            rs.getInt("THIGH"),
                            rs.getInt("HEM"),
                            rs.getInt("RISE")),
                    productId);
        }else{
            getSizeQuery =
                            "select SIZE_TYPE\n" +
                            "from PRODUCT_SIZE, PRODUCT\n" +
                            "where PRODUCT.PRODUCT_ID=? and PRODUCT.DIV_ID = PRODUCT_SIZE.DIV_ID;";
            return this.jdbcTemplate.query(getSizeQuery,
                    (rs, rowNum) -> new GetSizeRes(
                            rs.getString("SIZE_TYPE")),
                    productId);
        }
    }
}

package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.shipment.model.GetShipmentRes;
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
        String getShipmentQuery =
                "select PRODUCT_IMAGE,BRAND_NAME,PRODUCT_NAME,PRODUCT_PRICE\n" +
                "from PRODUCT inner join BRAND\n" +
                "on PRODUCT.BRAND_ID = BRAND.BRAND_ID and DIV_ID = ?;";

        return this.jdbcTemplate.query(getShipmentQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getString("PRODUCT_IMAGE"),
                        rs.getString("BRAND_NAME"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getInt("PRODUCT_PRICE")),
                divId);
    }
    public List<GetProductRes> getProductsWithCategory(int divId,Integer categoryId) {
        String getShipmentQuery =
                        "select PRODUCT_IMAGE,BRAND_NAME,PRODUCT_NAME,PRODUCT_PRICE\n" +
                        "from PRODUCT inner join BRAND\n" +
                        "on PRODUCT.BRAND_ID = BRAND.BRAND_ID and DIV_ID = ? and CATEGORY_ID = ?;";

        return this.jdbcTemplate.query(getShipmentQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getString("PRODUCT_IMAGE"),
                        rs.getString("BRAND_NAME"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getInt("PRODUCT_PRICE")),
                divId,categoryId);
    }
}

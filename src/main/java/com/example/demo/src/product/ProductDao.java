package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductRes> getProducts(int divId,Integer pageNum) {

        String getProductQuery =
                        "select PRODUCT_IMAGE,BRAND_NAME,PRODUCT_NAME,PRODUCT_PRICE\n" +
                        "from PRODUCT inner join BRAND\n" +
                        "on PRODUCT.BRAND_ID = BRAND.BRAND_ID and DIV_ID = ?\n" +
                        "limit ?,5;";

        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getString("PRODUCT_IMAGE"),
                        rs.getString("BRAND_NAME"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getInt("PRODUCT_PRICE")),
                divId,(pageNum-1)*5);
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

    public PostProductRes postProduct(PostProductReq postProductReq){
        //브랜드ID 추출
        int brandId = this.jdbcTemplate.queryForObject("select BRAND_ID from brand where BRAND_NAME=?;"
                                                            ,int.class,postProductReq.getBrandName());
        //상품구분ID 추출
        int productDivId = this.jdbcTemplate.queryForObject("select DIV_ID from product_div where DIV_NAME=?;"
                ,int.class,postProductReq.getDivName());
        //카테고리ID 추출
        int categoryId = this.jdbcTemplate.queryForObject("select CATEGORY_ID from category where CATEGORY_NAME=?;"
                ,int.class,postProductReq.getCategoryName());

        String InsertProductQuery =
                "insert into product(BRAND_ID, DIV_ID, CATEGORY_ID, PRODUCT_PRICE, PRODUCT_NAME, PRODUCT_IMAGE, PRODUCT_INFO, CREATED_AT, UPDATED_AT, STATUS)\n" +
                "values (?,?,?,?,?,?,?,NOW(),NOW(),1);";
        Object[] InsertProductParams = new Object[]{
                brandId,
                productDivId,
                categoryId,
                postProductReq.getProductPrice(),
                postProductReq.getProductName(),
                postProductReq.getProductImage(),
                postProductReq.getProductInfo()
        };
        this.jdbcTemplate.update(InsertProductQuery, InsertProductParams);
        int productId = this.jdbcTemplate.queryForObject("select PRODUCT_ID from product order by PRODUCT_ID desc limit 1;",int.class);
        postProductReq.getSizeTypeAndStocks().forEach((key,value)->{System.out.println(key+" "+value);});
        postProductReq.getSizeTypeAndStocks().forEach(
                (key,value)->{
                    int sizeId = traceSizeId((String) key);
                    String InsertStockQuery =
                            "insert into stock(product_id, size_id, stock) values (?,?,?);";
                    Object[] InsertStockParams = new Object[]{
                            productId,
                            sizeId,
                            value
                    };
                    this.jdbcTemplate.update(InsertStockQuery, InsertStockParams);
                });
        String responsePostInfoQuery =
                "select BRAND_ID, DIV_ID, CATEGORY_ID, PRODUCT_PRICE, PRODUCT_NAME, PRODUCT_IMAGE, PRODUCT_INFO\n" +
                "from product\n" +
                "order by PRODUCT_ID desc\n" +
                "limit 1;";
        return this.jdbcTemplate.queryForObject(responsePostInfoQuery,
                (rs, rowNum) -> new PostProductRes(
                        rs.getInt("BRAND_ID"),
                        rs.getInt("DIV_ID"),
                        rs.getInt("CATEGORY_ID"),
                        rs.getInt("PRODUCT_PRICE"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getString("PRODUCT_IMAGE"),
                        rs.getString("PRODUCT_INFO"))
                );
    }

    public int traceSizeId(String sizeType){
        return this.jdbcTemplate.queryForObject("select SIZE_ID from product_size where SIZE_TYPE=?;",int.class,sizeType);
    }
}

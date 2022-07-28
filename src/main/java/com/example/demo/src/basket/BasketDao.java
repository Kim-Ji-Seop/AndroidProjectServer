package com.example.demo.src.basket;

import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.basket.model.PostAddProductReq;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BasketDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetBasketRes> getBasket(int userId) {
        String getBasketQuery =
                                "select u.USER_ID,u.USER_NAME,p.PRODUCT_IMAGE,p.PRODUCT_INFO,p.PRODUCT_NAME,ps.SIZE_TYPE,s.STOCK,p.PRODUCT_PRICE,b.PRODUCT_QUANTITY,p.PRODUCT_PRICE*b.PRODUCT_QUANTITY as TOTAL\n" +
                                "from BASKET b\n" +
                                "inner join USER u on b.USER_ID = u.USER_ID\n" +
                                "inner join PRODUCT p on b.PRODUCT_ID = p.PRODUCT_ID\n" +
                                "inner join PRODUCT_SIZE ps on b.SIZE_ID = ps.SIZE_ID\n" +
                                "inner join STOCK s on b.PRODUCT_ID = s.PRODUCT_ID and b.SIZE_ID=s.SIZE_ID and p.PRODUCT_ID=s.PRODUCT_ID\n" +
                                "where u.USER_ID=?;";

        return this.jdbcTemplate.query(getBasketQuery,
                (rs, rowNum) -> new GetBasketRes(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_NAME"),
                        rs.getString("PRODUCT_IMAGE"),
                        rs.getString("PRODUCT_INFO"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getString("SIZE_TYPE"),
                        rs.getInt("STOCK"),
                        rs.getInt("PRODUCT_PRICE"),
                        rs.getInt("PRODUCT_QUANTITY"),
                        rs.getInt("TOTAL")),
                userId);
    }
    public int addBasket(int userId, int productId, PostAddProductReq postAddProductReq){
        String Query =
                "insert into BASKET (USER_ID, PRODUCT_ID, SIZE_ID,PRODUCT_QUANTITY, CREATED_AT, UPDATED_AT, STATUS)\n" +
                "values (?,?,?,?,NOW(),NOW(),1);";
        Object[] Params = new Object[]{
                userId,
                productId,
                postAddProductReq.getSizeId(),
                postAddProductReq.getProductQuantity()
        };
        this.jdbcTemplate.update(Query, Params);

        String lastInsertIdQuery = "select BASKET_ID from BASKET order by BASKET_ID desc limit 1;";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}

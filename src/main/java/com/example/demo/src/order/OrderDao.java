package com.example.demo.src.order;

import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.src.product.model.GetSizeRes;
import com.example.demo.src.product.model.GetTopSizeRes;
import com.example.demo.src.shipment.model.PostShipmentReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<PostOrderRes> doOrder(int userIdx, List<Integer> basketId, PostOrderReq postOrderReq){
        for(int i=0;i<basketId.size();i++){
            String addOrderQuery =
                            "insert into `ORDER`(BASKET_ID, BUY_ID,REQUEST, CREATED_AT, UPDATED_AT, STATUS)\n" +
                            "values (?,concat(DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'),concat('000',BASKET_ID)),?,NOW(),NOW(),1);";
            Object[] addOrderParams = new Object[]{
                    basketId.get(i),
                    postOrderReq.getRequest()
            };
            this.jdbcTemplate.update(addOrderQuery,addOrderParams);
        }
        String InsertIdQuery =
                        "select o.BASKET_ID,o.BUY_ID,o.REQUEST\n" +
                        "from `ORDER` o\n" +
                        "inner join BASKET b\n" +
                        "on b.BASKET_ID=o.BASKET_ID\n" +
                        "where b.USER_ID=?;";
        return this.jdbcTemplate.query(InsertIdQuery,
                (rs, rowNum) -> new PostOrderRes(
                        rs.getInt("BASKET_ID"),
                        rs.getLong("BUY_ID"),
                        rs.getString("REQUEST")),
                userIdx);
    }
    public List<GetOrderRes> getHistory(int userIdx){
        String Query = "select BRAND_NAME,PRODUCT_NAME,ps.SIZE_TYPE,DATE(o.CREATED_AT) as ORDERED_DAY,o.BUY_ID,p.PRODUCT_PRICE*b.PRODUCT_QUANTITY as TOTAL,b.PRODUCT_QUANTITY\n" +
                "from `ORDER` o\n" +
                "inner join BASKET b on o.BASKET_ID = b.BASKET_ID\n" +
                "inner join USER u on u.USER_ID = b.USER_ID\n" +
                "inner join PRODUCT p on b.PRODUCT_ID = p.PRODUCT_ID\n" +
                "inner join PRODUCT_SIZE ps on b.SIZE_ID = ps.SIZE_ID\n" +
                "inner join BRAND b2 on p.BRAND_ID = b2.BRAND_ID\n" +
                "inner join STOCK s on b.PRODUCT_ID = s.PRODUCT_ID and b.SIZE_ID=s.SIZE_ID and p.PRODUCT_ID=s.PRODUCT_ID\n" +
                "where u.USER_ID=?;";
        return this.jdbcTemplate.query(Query,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getString("BRAND_NAME"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getString("SIZE_TYPE"),
                        rs.getDate("ORDERED_DAY"),
                        rs.getLong("BUY_ID"),
                        rs.getInt("TOTAL"),
                        rs.getInt("PRODUCT_QUANTITY")),
                userIdx);
    }
}

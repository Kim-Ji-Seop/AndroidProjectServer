package com.example.demo.src.order;

import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.src.product.model.GetSizeRes;
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
}

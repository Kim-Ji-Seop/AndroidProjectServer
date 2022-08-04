package com.example.demo.src.order;

import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderFromBasketReq;
import com.example.demo.src.order.model.PostOrderFromBasketRes;
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

    public List<PostOrderFromBasketRes> doOrder(int userIdx, List<Integer> basketId, PostOrderFromBasketReq postOrderFromBasketReq){
        //장바구니에 담은 물품 결제
        if(basketId != null){
            for (Integer i : basketId) {
                String addOrderQuery =
                                "insert into `ORDER`(BUY_ID,BASKET_ID,USER_ID,REQUEST, CREATED_AT, UPDATED_AT, STATUS)\n" +
                                "values (concat(DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'),concat('000',BASKET_ID)),?,?,?,NOW(),NOW(),1);";
                Object[] addOrderParams = new Object[]{
                        i,
                        userIdx,
                        postOrderFromBasketReq.getRequest()
                };
                this.jdbcTemplate.update(addOrderQuery, addOrderParams);
                deductStockwithBasket(i); //재고량 연산

            }
        }
//        for(int i=0;i<basketId.size();i++){
//            String addOrderQuery =
//                            "insert into `ORDER`(BASKET_ID, BUY_ID,REQUEST, CREATED_AT, UPDATED_AT, STATUS)\n" +
//                            "values (?,concat(DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'),concat('000',BASKET_ID)),?,NOW(),NOW(),1);";
//            Object[] addOrderParams = new Object[]{
//                    basketId.get(i),
//                    postOrderReq.getRequest()
//            };
//            this.jdbcTemplate.update(addOrderQuery,addOrderParams);
//        }
        String InsertIdQuery =
                        "select o.BASKET_ID,o.BUY_ID,o.REQUEST\n" +
                        "from `ORDER` o\n" +
                        "inner join BASKET b\n" +
                        "on b.BASKET_ID=o.BASKET_ID\n" +
                        "where b.USER_ID=?;";
        return this.jdbcTemplate.query(InsertIdQuery,
                (rs, rowNum) -> new PostOrderFromBasketRes(
                        rs.getInt("BASKET_ID"),
                        rs.getLong("BUY_ID"),
                        rs.getString("REQUEST")),
                userIdx);
    }
    public List<GetOrderRes> getHistory(int userIdx){
        String Query =
                "select BRAND_NAME,PRODUCT_NAME,ps.SIZE_TYPE,DATE(o.CREATED_AT) as ORDERED_DAY,o.BUY_ID,p.PRODUCT_PRICE*b.PRODUCT_QUANTITY as TOTAL,b.PRODUCT_QUANTITY\n" +
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
    //재고량 차감 함수
    public void deductStockwithBasket(int basketId){
        int totalStock = this.jdbcTemplate.queryForObject( //총 재고량
                    "select s.STOCK\n" +
                            "from stock s\n" +
                                   "inner join basket b on b.PRODUCT_ID = s.PRODUCT_ID and b.SIZE_ID=s.SIZE_ID\n" +
                            "where b.BASKET_ID=?;",
                int.class, basketId);
        int quantity=this.jdbcTemplate.queryForObject( // 상품 수량
                    "select b.PRODUCT_QUANTITY\n" +
                            "from basket b\n" +
                            "where b.BASKET_ID=?;",
                int.class, basketId);

        String inactiveUserStatusQuery =
                "update STOCK s set s.STOCK = ?  where s.PRODUCT_ID= (select * from (select b.PRODUCT_ID as PRODUCT_ID from stock s inner join basket b on b.PRODUCT_ID = s.PRODUCT_ID and b.SIZE_ID=s.SIZE_ID where b.BASKET_ID=?) as temp) and\n" +
                "s.SIZE_ID = (select * from(select b.SIZE_ID as SIZE_ID from stock s inner join basket b on b.PRODUCT_ID = s.PRODUCT_ID and b.SIZE_ID=s.SIZE_ID where b.BASKET_ID=?) as temp1 );";
        Object[] inactiveUserStatusParams = new Object[]{totalStock-quantity,basketId,basketId};
        this.jdbcTemplate.update(inactiveUserStatusQuery,inactiveUserStatusParams);
    }

}

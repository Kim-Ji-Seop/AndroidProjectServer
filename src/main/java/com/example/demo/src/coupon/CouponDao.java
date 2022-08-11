package com.example.demo.src.coupon;

import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.coupon.model.*;
import com.example.demo.src.order.model.PostOrderFromBasketRes;
import com.example.demo.src.product.model.PostProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CouponDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PostCouponRes postCoupon(PostCouponReq postCouponReq){
        int brandId = this.jdbcTemplate.queryForObject("select BRAND_ID from brand where BRAND_NAME=?;",int.class,postCouponReq.getBrandName());
        String Query =
                                "insert into coupon(BRAND_ID, COUPON_NAME, DISCOUNT_RATE, DISCOUNT_INFO, COUPON_END, CREATED_AT, UPDATED_AT, STATUS)\n" +
                                "values (?,?,?,concat(DATE_FORMAT(now(),'%Y-%m-%d'),' ~ ',DATE_ADD(DATE_FORMAT(now(),'%Y.%m.%d'), INTERVAL 30 DAY)),DATE_ADD(DATE_FORMAT(now(),'%Y.%m.%d'), INTERVAL 30 DAY),NOW(),NOW(),1);";
        Object[] Params = new Object[]{
                brandId,
                postCouponReq.getCouponName(),
                postCouponReq.getDiscountRate()
        };
        this.jdbcTemplate.update(Query, Params);

        String lastInsertIdQuery = "select BRAND_ID,COUPON_NAME,DISCOUNT_RATE,DISCOUNT_INFO from coupon order by COUPON_ID desc limit 1;";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,
                (rs, rowNum) -> new PostCouponRes(
                        rs.getInt("BRAND_ID"),
                        rs.getString("COUPON_NAME"),
                        rs.getInt("DISCOUNT_RATE"),
                        rs.getString("DISCOUNT_INFO")));
    }

    public List<GetCouponRes> getCoupon(){
        String getCouponQuery =
                        "select COUPON_NAME,DISCOUNT_INFO,concat(DISCOUNT_RATE,'%') as DISCOUNT_RATE,BRAND_IMAGE\n" +
                        "from coupon c\n" +
                        "inner join brand b on c.BRAND_ID = b.BRAND_ID;";

        return this.jdbcTemplate.query(getCouponQuery,
                (rs, rowNum) -> new GetCouponRes(
                        rs.getString("COUPON_NAME"),
                        rs.getString("DISCOUNT_INFO"),
                        rs.getString("DISCOUNT_RATE"),
                        rs.getString("BRAND_IMAGE"))
                );
    }

    public PostCouponIndividualRes postCouponIndividual(int userIdx,PostCouponIndividualReq postCouponIndividualReq){
        String InsertUserCouponQuery = "insert into user_coupon(USER_ID, COUPON_ID) values(?,?);";
        Object[] InsertUserParams = new Object[]{
                userIdx,
                postCouponIndividualReq.getCouponId()
        };
        this.jdbcTemplate.update(InsertUserCouponQuery,InsertUserParams);

        String LastAddUserCouponQuery =
                "select c.COUPON_NAME,c.DISCOUNT_INFO,concat(DISCOUNT_RATE,'%') as DISCOUNT_RATE,b.BRAND_IMAGE\n" +
                "from coupon c\n" +
                "inner join brand b on c.BRAND_ID = b.BRAND_ID\n" +
                "inner join user_coupon uc on c.COUPON_ID = uc.COUPON_ID and USER_ID=? and uc.COUPON_ID=?;";
        return this.jdbcTemplate.queryForObject(LastAddUserCouponQuery,
                (rs, rowNum) -> new PostCouponIndividualRes(
                        rs.getString("COUPON_NAME"),
                        rs.getString("DISCOUNT_INFO"),
                        rs.getString("DISCOUNT_RATE"),
                        rs.getString("BRAND_IMAGE"))
                ,userIdx,postCouponIndividualReq.getCouponId());
    }

    public List<GetCouponIndividualRes> getCouponIndividual(int userIdx){
        String getCouponQuery =
                "select c.COUPON_NAME,c.DISCOUNT_INFO,concat(DISCOUNT_RATE,'%') as DISCOUNT_RATE,b.BRAND_IMAGE\n" +
                        "from coupon c\n" +
                        "inner join brand b on c.BRAND_ID = b.BRAND_ID\n" +
                        "inner join user_coupon uc on c.COUPON_ID = uc.COUPON_ID and USER_ID=?;";

        return this.jdbcTemplate.query(getCouponQuery,
                (rs, rowNum) -> new GetCouponIndividualRes(
                        rs.getString("COUPON_NAME"),
                        rs.getString("DISCOUNT_INFO"),
                        rs.getString("DISCOUNT_RATE"),
                        rs.getString("BRAND_IMAGE"))
                ,userIdx);
    }
}

package com.example.demo.src.order;

import com.example.demo.src.basket.model.Basket;
import com.example.demo.src.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 트랜잭션 1 - 서비스 계층에서 어노테이션
     * 장바구니 담기 후 구매
     * 물품 -> 장바구니 -> 결제api -> 재고량 차감 -> 장바구니에서 삭제(DELETE)(api x) -> 주문내역 추가
     * 롤백, 커밋
     */
    public List<PostOrderFromBasketRes> doOrderFromBasket(int userIdx, PostOrderFromBasketReq postOrderFromBasketReq){
        List<Integer> basketId = postOrderFromBasketReq.getBasketId();
        Basket basketList;
        //장바구니에 담은 물품 결제
        if(basketId != null){
            for (Integer i : basketId) {
                basketList = getBasketInfo(userIdx,i);
                deductStockwithBasket(i); //재고량 연산
                deleteBasketInfo(i); //장바구니 정보 수정 status 1->0
                String addOrderQuery =
                                "insert into `order`(BUY_ID,BASKET_ID,USER_ID,PRODUCT_ID,SIZE_ID,PRODUCT_QUANTITY, CREATED_AT, UPDATED_AT, STATUS)\n" +
                                "values (cast(concat(DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'),concat('000',?)) as unsigned integer),?,?,?,?,?,NOW(),NOW(),1);";
                Object[] addOrderParams = new Object[]{
                        basketList.getBasketId(),
                        basketList.getBasketId(),
                        basketList.getUserId(),
                        basketList.getProductId(),
                        basketList.getSizeId(),
                        basketList.getProductQuantity()
                };
                this.jdbcTemplate.update(addOrderQuery, addOrderParams);
            }
        }
        String InsertIdQuery =
                        "select o.BASKET_ID,o.BUY_ID\n" +
                        "from `ORDER` o\n" +
                        "inner join BASKET b\n" +
                        "on b.BASKET_ID=o.BASKET_ID\n" +
                        "where b.USER_ID=?;";
        return this.jdbcTemplate.query(InsertIdQuery,
                (rs, rowNum) -> new PostOrderFromBasketRes(
                        rs.getLong("BUY_ID"),
                        rs.getInt("BASKET_ID")),
                userIdx);
    }
    public Basket getBasketInfo(int userIdx,int basketIdx){
        String checkQuery = "select * from basket where USER_ID=? and BASKET_ID=?;";
        return this.jdbcTemplate.queryForObject(checkQuery,
                (rs, rowNum) -> new Basket(
                        rs.getInt("BASKET_ID"),
                        rs.getInt("USER_ID"),
                        rs.getInt("PRODUCT_ID"),
                        rs.getInt("SIZE_ID"),
                        rs.getInt("PRODUCT_QUANTITY"),
                        rs.getTimestamp("CREATED_AT"),
                        rs.getTimestamp("UPDATED_AT"),
                        rs.getInt("STATUS")),
                userIdx,basketIdx);
    }
    /**
     * 트랜잭션 2 - 서비스 계층에서 어노테이션
     * 바로구매
     * 물품 -> 결제api -> 재고량 차감 -> 주문내역 추가
     * 롤백, 커밋
     *
     */
    public PostOrderRes doOrder(int userIdx, PostOrderReq postOrderReq){
        deductStock(postOrderReq.getProductId(), postOrderReq.getProductQuantity(),postOrderReq.getSizeType()); // 재고량 차감
        int lastOrderId = this.jdbcTemplate.queryForObject( // 주문 전 마지막 orderId
                "select ORDER_ID+1 from `order` ORDER BY ORDER_ID DESC LIMIT 1;",
                int.class);
        String doOrderQuery =
                "insert into `order`(BASKET_ID, USER_ID, PRODUCT_ID,SIZE_ID,BUY_ID,PRODUCT_QUANTITY, CREATED_AT, UPDATED_AT, STATUS)\n" +
                        "            values (null,?,?,null,cast(concat(DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'),concat('000',?)) as unsigned integer),?,NOW(),NOW(),1);";
        Object[] doOrderParams = new Object[]{
                userIdx,
                postOrderReq.getProductId(),
                lastOrderId,
                postOrderReq.getProductQuantity()
        };
        this.jdbcTemplate.update(doOrderQuery,doOrderParams);
        insertSizeId(postOrderReq.getSizeType(), postOrderReq.getProductId()); // order 테이블에 sizeId 기입
        String InsertLastOrderQuery =
                        "select PRODUCT_ID,PRODUCT_QUANTITY\n" +
                        "from `order`\n" +
                        "ORDER BY BUY_ID desc\n" +
                        "limit 1;";
        return this.jdbcTemplate.queryForObject(InsertLastOrderQuery,
                (rs, rowNum) -> new PostOrderRes(
                        rs.getInt("PRODUCT_ID"),
                        rs.getInt("PRODUCT_QUANTITY"))
                );
    }
    // 브랜드 명, 상품 명, 사이즈 타입, 주문일자, 주문번호, 총 주문금액, 수량
    // 커서 페이징 -> 근데 여기서 커서페이징은 필요없을듯?
    public List<GetOrderRes> getHistory(int userIdx,int cursor){
        String Query =
                        "select (select BRAND_NAME from brand b where p.BRAND_ID=b.BRAND_ID) as BRAND_NAME,p.PRODUCT_NAME,ps.SIZE_TYPE,DATE(o.CREATED_AT) as ORDERED_DAY,o.BUY_ID,p.PRODUCT_PRICE*o.PRODUCT_QUANTITY as TOTAL_PRICE,o.PRODUCT_QUANTITY\n" +
                        "from `order` o\n" +
                        "inner join product p on o.PRODUCT_ID = p.PRODUCT_ID\n" +
                        "inner join product_div pd on p.DIV_ID = pd.DIV_ID\n" +
                        "inner join product_size ps on o.SIZE_ID = ps.SIZE_ID\n" +
                        "where USER_ID=? and ORDER_ID <= ?\n" +
                        "ORDER BY BUY_ID DESC\n" +
                        "LIMIT 5;";
        return this.jdbcTemplate.query(Query,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getString("BRAND_NAME"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getString("SIZE_TYPE"),
                        rs.getDate("ORDERED_DAY"),
                        rs.getLong("BUY_ID"),
                        rs.getInt("TOTAL_PRICE"),
                        rs.getInt("PRODUCT_QUANTITY")),
                userIdx,cursor);
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

        String query =
                "update STOCK s set s.STOCK = ?  where s.PRODUCT_ID= (select * from (select b.PRODUCT_ID as PRODUCT_ID from stock s inner join basket b on b.PRODUCT_ID = s.PRODUCT_ID and b.SIZE_ID=s.SIZE_ID where b.BASKET_ID=?) as temp) and\n" +
                "s.SIZE_ID = (select * from(select b.SIZE_ID as SIZE_ID from stock s inner join basket b on b.PRODUCT_ID = s.PRODUCT_ID and b.SIZE_ID=s.SIZE_ID where b.BASKET_ID=?) as temp1 );";
        Object[] params = new Object[]{totalStock-quantity,basketId,basketId};
        this.jdbcTemplate.update(query,params);
    }
    //장바구니 물건 삭제 -> 해당 basketId의 status값 0으로 전환
    public void deleteBasketInfo(int basketId){
        String query = "update basket set STATUS=0 where BASKET_ID=?;";
        Object[] params = new Object[]{basketId};
        this.jdbcTemplate.update(query,params);
    }
    //바로구매 재고량 차감
    public void deductStock(int productId,int productQuantity,String sizeType){
        int totalStock = this.jdbcTemplate.queryForObject( //총 재고량
                        "select STOCK\n" +
                        "from STOCK\n" +
                        "where SIZE_ID = (select SIZE_ID from product_size where SIZE_TYPE=?) and PRODUCT_ID = ?;",
                        int.class, sizeType,productId);
        String query =
                "update stock s\n" +
                "set s.STOCK=?\n" +
                "where s.SIZE_ID = (select SIZE_ID from product_size where SIZE_TYPE=?) and s.PRODUCT_ID = ?;";
        Object[] params = new Object[]{totalStock-productQuantity,sizeType,productId};
        this.jdbcTemplate.update(query,params);
    }
    public void insertSizeId(String sizeType, int productId){
        String doOrderQuery =
                        "update `order` o set SIZE_ID = (select SIZE_ID\n" +
                        "from stock s\n" +
                        "where s.SIZE_ID = (select SIZE_ID from product_size where SIZE_TYPE=?) and s.PRODUCT_ID = ?)\n" +
                        "order by BUY_ID desc limit 1;";
        Object[] doOrderParams = new Object[]{
                sizeType,productId
        };
        this.jdbcTemplate.update(doOrderQuery,doOrderParams);
    }
}

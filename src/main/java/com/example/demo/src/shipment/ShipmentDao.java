package com.example.demo.src.shipment;

import com.example.demo.src.shipment.model.GetShipmentRes;
import com.example.demo.src.shipment.model.PostShipmentReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class ShipmentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetShipmentRes> getShipment(int userIdx) {
        String getShipmentQuery = "SELECT u.USER_NAME,s.SHIPMENT_NAME,s.ADDRESS,u.PHONE_NUMBER FROM shipment s INNER JOIN user u ON u.USER_ID = s.USER_ID WHERE u.USER_ID = ?;";

        return this.jdbcTemplate.query(getShipmentQuery,
                (rs, rowNum) -> new GetShipmentRes(
                        rs.getString("USER_NAME"),
                        rs.getString("SHIPMENT_NAME"),
                        rs.getString("ADDRESS"),
                        rs.getString("PHONE_NUMBER")),
                userIdx);
    }
    public int addShipment(int userIdx, PostShipmentReq postShipmentReq){
        String addShipmentQuery = "insert into shipment(USER_ID,SHIPMENT_NAME,ADDRESS,ZIPCODE,CREATED_AT,UPDATED_AT,STATUS) values (?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%d'),DATE_FORMAT(NOW(),'%Y-%m-%d'),1);";
        Object[] addShipmentParams = new Object[]{
                userIdx,
                postShipmentReq.getShipmentName(),
                postShipmentReq.getAddress(),
                postShipmentReq.getZipcode()
                };
        this.jdbcTemplate.update(addShipmentQuery,addShipmentParams);
        String lastInsertIdQuery = "select SHIPMENT_ID from shipment order by SHIPMENT_ID desc limit 1;";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}

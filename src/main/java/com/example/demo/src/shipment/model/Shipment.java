package com.example.demo.src.shipment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class Shipment {
    private int shipmentID;
    private int userId;
    private String shipmentName;
    private String address;
    private String zipcode;
    private Date createdAt;
    private Date updateAt;
    private int status;
}

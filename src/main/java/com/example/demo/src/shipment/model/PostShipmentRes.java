package com.example.demo.src.shipment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostShipmentRes {
    private int shipmentId;
    private int userId;
    private String shipmentName;
    private String address;
    private String zipcode;
}

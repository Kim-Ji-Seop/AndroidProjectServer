package com.example.demo.src.shipment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostShipmentReq {
    private String shipmentName;
    private String address;
    private String zipcode;
}

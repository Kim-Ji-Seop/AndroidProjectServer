package com.example.demo.src.shipment.model;

import com.example.demo.src.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetShipmentRes {
    private String userName;
    private String shipmentName;
    private String address;
    private String phoneNumber;
}

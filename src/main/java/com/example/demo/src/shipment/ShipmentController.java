package com.example.demo.src.shipment;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.shipment.model.GetShipmentRes;
import com.example.demo.src.shipment.model.PostShipmentReq;
import com.example.demo.src.shipment.model.PostShipmentRes;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/delivery")
public class ShipmentController {
    @Autowired
    private final ShipmentProvider shipmentProvider;
    @Autowired
    private final ShipmentService shipmentService;
    @Autowired
    private final JwtService jwtService;

    public ShipmentController(ShipmentProvider shipmentProvider, ShipmentService shipmentService,JwtService jwtService){
        this.shipmentProvider = shipmentProvider;
        this.shipmentService = shipmentService;
        this.jwtService = jwtService;
    }
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/delivery
    public BaseResponse<List<GetShipmentRes>> getShipment() {
        // Get Users
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetShipmentRes> getShipmentRes = shipmentProvider.getShipment(userIdx);
            return new BaseResponse<>(getShipmentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PostMapping("") // (GET) 127.0.0.1:9000/app/delivery
    public BaseResponse<PostShipmentRes> addShipment(@RequestBody PostShipmentReq postShipmentReq) {
        // Get
        try{
            int userIdx = jwtService.getUserIdx();
            PostShipmentRes postShipmentRes = shipmentService.addShipment(userIdx,postShipmentReq);
            return new BaseResponse<>(postShipmentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

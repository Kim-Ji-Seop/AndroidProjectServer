package com.example.demo.src.shipment;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.shipment.model.GetShipmentRes;
import com.example.demo.src.shipment.model.PostShipmentReq;
import com.example.demo.src.shipment.model.PostShipmentRes;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostUserReq;
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

    public ShipmentController(ShipmentProvider shipmentProvider, ShipmentService shipmentService){
        this.shipmentProvider = shipmentProvider;
        this.shipmentService = shipmentService;
    }
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/delivery/{userIdx}
    public BaseResponse<List<GetShipmentRes>> getShipment(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            List<GetShipmentRes> getShipmentRes = shipmentProvider.getShipment(userIdx);
            return new BaseResponse<>(getShipmentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PostMapping("/{userIdx}/add") // (GET) 127.0.0.1:9000/app/delivery/{userIdx}/add
    public BaseResponse<PostShipmentRes> addShipment(@PathVariable("userIdx") int userIdx,@RequestBody PostShipmentReq postShipmentReq) {
        // Get
        try{
            PostShipmentRes postShipmentRes = shipmentService.addShipment(userIdx,postShipmentReq);
            return new BaseResponse<>(postShipmentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

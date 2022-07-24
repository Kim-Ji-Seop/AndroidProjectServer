package com.example.demo.src.shipment;

import com.example.demo.config.BaseException;
import com.example.demo.src.shipment.model.PostShipmentReq;
import com.example.demo.src.shipment.model.PostShipmentRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ShipmentService {
    private final ShipmentDao shipmentDao;
    private final ShipmentProvider shipmentProvider;

    @Autowired
    public ShipmentService(ShipmentDao shipmentDao, ShipmentProvider shipmentProvider, JwtService jwtService) {
        this.shipmentDao = shipmentDao;
        this.shipmentProvider = shipmentProvider;
    }

    public PostShipmentRes addShipment(int userIdx,PostShipmentReq postShipmentReq) throws BaseException{
        try {
            return new PostShipmentRes(
                    shipmentDao.addShipment(userIdx, postShipmentReq),
                    userIdx,
                    postShipmentReq.getShipmentName(),
                    postShipmentReq.getAddress(),
                    postShipmentReq.getZipcode()
            );
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

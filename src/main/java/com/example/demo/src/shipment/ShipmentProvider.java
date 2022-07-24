package com.example.demo.src.shipment;

import com.example.demo.config.BaseException;
import com.example.demo.src.shipment.model.GetShipmentRes;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ShipmentProvider {
    private final ShipmentDao shipmentDao;

    @Autowired
    public ShipmentProvider(ShipmentDao shipmentDao){
        this.shipmentDao=shipmentDao;
    }
    //개인회원 배송지정보 조회
    public List<GetShipmentRes> getShipment(int userIdx) throws BaseException {
        try {
            return shipmentDao.getShipment(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

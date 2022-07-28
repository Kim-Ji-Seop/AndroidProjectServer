package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.ProductProvider;
import com.example.demo.src.shipment.model.PostShipmentReq;
import com.example.demo.src.shipment.model.PostShipmentRes;
import com.jogamp.common.util.ArrayHashSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderProvider orderProvider;

    public OrderService(OrderDao orderDao, OrderProvider orderProvider) {
        this.orderDao = orderDao;
        this.orderProvider = orderProvider;
    }

    public List<PostOrderRes> doOrder(int userIdx, List<Integer> basketId, PostOrderReq postOrderReq) throws BaseException {
        try {
            return orderDao.doOrder(userIdx,basketId,postOrderReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
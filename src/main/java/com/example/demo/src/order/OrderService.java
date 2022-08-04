package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.PostOrderFromBasketReq;
import com.example.demo.src.order.model.PostOrderFromBasketRes;
import org.springframework.stereotype.Service;

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

    public List<PostOrderFromBasketRes> doOrder(int userIdx, List<Integer> basketId, PostOrderFromBasketReq postOrderFromBasketReq) throws BaseException {
        try {
            return orderDao.doOrder(userIdx,basketId, postOrderFromBasketReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
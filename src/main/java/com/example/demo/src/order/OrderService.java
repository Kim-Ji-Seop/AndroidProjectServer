package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.PostOrderFromBasketReq;
import com.example.demo.src.order.model.PostOrderFromBasketRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public List<PostOrderFromBasketRes> doOrderFromBasket(int userIdx, PostOrderFromBasketReq postOrderFromBasketReq) throws BaseException {
        try {
            return orderDao.doOrderFromBasket(userIdx, postOrderFromBasketReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public PostOrderRes doOrder(int userIdx, PostOrderReq postOrderReq) throws BaseException {
        try {
            return orderDao.doOrder(userIdx,postOrderReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
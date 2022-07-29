package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.model.GetProductRes;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class OrderProvider {
    private final OrderDao orderDao;
    public OrderProvider(OrderDao orderDao){
        this.orderDao=orderDao;
    }

    public List<GetOrderRes> getHistory(int userIdx) throws BaseException {
        try {
            return orderDao.getHistory(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

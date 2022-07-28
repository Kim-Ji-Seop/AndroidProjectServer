package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.model.GetProductRes;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class BasketProvider {
    private final BasketDao basketDao;
    public BasketProvider(BasketDao basketDao){
        this.basketDao=basketDao;
    }

    public List<GetBasketRes> getBasket(int userId) throws BaseException {
        try {
            return basketDao.getBasket(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

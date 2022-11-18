package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.model.PostBasketProductReq;
import com.example.demo.src.basket.model.PostBasketProductRes;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BasketService {
    private final BasketDao basketDao;
    private final BasketProvider basketProvider;
    public BasketService(BasketDao basketDao,BasketProvider basketProvider){
        this.basketDao=basketDao;
        this.basketProvider=basketProvider;
    }
    public PostBasketProductRes addBasket(int userId, int productId, PostBasketProductReq postBasketProductReq) throws BaseException {

        try{
            int basketIdx = basketDao.addBasket(userId,productId, postBasketProductReq);

            return new PostBasketProductRes(
                    basketIdx,
                    userId,
                    productId,
                    postBasketProductReq.getSizeId(),
                    postBasketProductReq.getProductQuantity()
            );
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

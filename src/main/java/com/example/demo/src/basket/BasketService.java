package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.model.PostAddProductReq;
import com.example.demo.src.basket.model.PostAddProductRes;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.ProductProvider;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
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
    public PostAddProductRes addBasket(int userId, int productId, PostAddProductReq postAddProductReq) throws BaseException {

        try{
            int basketIdx = basketDao.addBasket(userId,productId,postAddProductReq);

            return new PostAddProductRes (
                    basketIdx,
                    userId,
                    productId,
                    postAddProductReq.getSizeId(),
                    postAddProductReq.getProductQuantity()
            );
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

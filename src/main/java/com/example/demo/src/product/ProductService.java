package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.model.PostBasketProductRes;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductService {
    private final ProductDao productDao;
    private final ProductProvider productProvider;
    public ProductService(ProductDao productDao,ProductProvider productProvider){
        this.productDao=productDao;
        this.productProvider=productProvider;
    }

    @Transactional(rollbackFor = Exception.class)
    public PostProductRes postProduct(PostProductReq postProductReq) throws BaseException {
        try {
            return productDao.postProduct(postProductReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

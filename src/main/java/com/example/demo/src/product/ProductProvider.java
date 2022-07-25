package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.shipment.model.GetShipmentRes;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductProvider {
    private final ProductDao productDao;
    public ProductProvider(ProductDao productDao){
        this.productDao=productDao;
    }

    public List<GetProductRes> getProducts(int divId) throws BaseException {
        try {
            return productDao.getProducts(divId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetProductRes> getProductsWithCategory(int divId,Integer categoryId) throws BaseException {
        try {
            return productDao.getProductsWithCategory(divId,categoryId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

package com.example.demo.src.product;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductDao productDao;
    private final ProductProvider productProvider;
    public ProductService(ProductDao productDao,ProductProvider productProvider){
        this.productDao=productDao;
        this.productProvider=productProvider;
    }
}

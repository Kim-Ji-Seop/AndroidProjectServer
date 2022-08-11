package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    public ProductController(ProductProvider productProvider, ProductService productService){
        this.productProvider = productProvider;
        this.productService = productService;
    }

    /**
     * 상의 카테고리 전체 물품 조회 API
     * [GET] /products/:divId
     * @return BaseResponse<List<GetProductRes>>
     */
    // Path-variable
    @ResponseBody              // (GET) localhost:9000/app/products/{divId}?category=
    @GetMapping("/{divId}") // (GET) localhost:9000/app/products/{divId}
    public BaseResponse<List<GetProductRes>> getproducts(@PathVariable("divId") int divId,@RequestParam(value = "category",required = false) Integer categoryId, @RequestParam(value = "page") Integer pageNum) {

        try{
            if(categoryId == null){
                List<GetProductRes> getProductRes = productProvider.getProducts(divId,pageNum);
                return new BaseResponse<>(getProductRes);
            }else{
                List<GetProductRes> getProductWithCategoryRes = productProvider.getProductsWithCategory(divId,categoryId);
                return new BaseResponse<>(getProductWithCategoryRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/goods/{productId}")
    public BaseResponse<GetGoodsRes> getGoods(@PathVariable("productId") int productId){
        try{
            GetGoodsRes getGoodsRes = productProvider.getGoods(productId);
            return new BaseResponse<>(getGoodsRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/goods/size/{productId}")
    public BaseResponse<List<GetSizeRes>> getSize(@PathVariable("productId") int productId){
        try{
            List<GetSizeRes> getSizeRes = productProvider.getSize(productId);
            return new BaseResponse<>(getSizeRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostProductRes> postProduct(@RequestBody PostProductReq postProductReq){
        try{
            PostProductRes postProductRes = productService.postProduct(postProductReq);
            return new BaseResponse<>(postProductRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}

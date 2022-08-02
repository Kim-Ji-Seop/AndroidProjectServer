package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.basket.model.PostAddProductReq;
import com.example.demo.src.basket.model.PostAddProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/cart")
public class BasketController {

    @Autowired
    private final BasketProvider basketProvider;
    @Autowired
    private final BasketService basketService;
    public BasketController(BasketProvider basketProvider, BasketService basketService){
        this.basketProvider = basketProvider;
        this.basketService = basketService;
    }
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetBasketRes>> getBasket(@PathVariable int userId){
        try{
            List<GetBasketRes> getBasketRes = basketProvider.getBasket(userId);
            return new BaseResponse<>(getBasketRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PostMapping("/{userId}/{productId}")
    public BaseResponse<PostAddProductRes> addBasket(@PathVariable int userId,@PathVariable int productId,@RequestBody PostAddProductReq postAddProductReq){
        try{
            PostAddProductRes postAddProductRes = basketService.addBasket(userId,productId,postAddProductReq);
            return new BaseResponse<>(postAddProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.basket.model.PostBasketProductReq;
import com.example.demo.src.basket.model.PostBasketProductRes;
import com.example.demo.utils.JwtService;
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
    @Autowired
    private final JwtService jwtService;
    public BasketController(BasketProvider basketProvider, BasketService basketService,JwtService jwtService){
        this.basketProvider = basketProvider;
        this.basketService = basketService;
        this.jwtService = jwtService;
    }
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetBasketRes>> getBasket(){
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetBasketRes> getBasketRes = basketProvider.getBasket(userIdx);
            return new BaseResponse<>(getBasketRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PostMapping("/{productId}")
    public BaseResponse<PostBasketProductRes> addBasket(@PathVariable int productId, @RequestBody PostBasketProductReq postBasketProductReq){
        try{
            int userIdx = jwtService.getUserIdx();
            PostBasketProductRes postBasketProductRes = basketService.addBasket(userIdx,productId, postBasketProductReq);
            return new BaseResponse<>(postBasketProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

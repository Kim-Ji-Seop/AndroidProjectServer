package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/order")
public class OrderController {
    @Autowired
    private final OrderProvider orderProvider;
    @Autowired
    private final OrderService orderService;
    public OrderController(OrderProvider orderProvider, OrderService orderService){
        this.orderProvider = orderProvider;
        this.orderService = orderService;
    }

    @ResponseBody
    @PostMapping("/basket/{userIdx}")
    public BaseResponse<List<PostOrderFromBasketRes>> doOrderFromBasket(@PathVariable("userIdx") int userIdx,@RequestBody PostOrderFromBasketReq postOrderFromBasketReq) {
        try{
            List<PostOrderFromBasketRes> postOrderFromBasketRes = orderService.doOrderFromBasket(userIdx, postOrderFromBasketReq);
            return new BaseResponse<>(postOrderFromBasketRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostOrderRes> doOrder(@PathVariable("userIdx") int userIdx,@RequestBody PostOrderReq postOrderReq) {
        try{
            PostOrderRes postOrderRes = orderService.doOrder(userIdx, postOrderReq);
            return new BaseResponse<>(postOrderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetOrderRes>> getHistory(@PathVariable("userIdx") int userIdx){
        try{
            List<GetOrderRes> getOrderRes = orderProvider.getHistory(userIdx);
            return new BaseResponse<>(getOrderRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}

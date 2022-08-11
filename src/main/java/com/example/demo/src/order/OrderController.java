package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
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
    @Autowired
    private final JwtService jwtService;
    public OrderController(OrderProvider orderProvider, OrderService orderService,JwtService jwtService){
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/basket")
    public BaseResponse<List<PostOrderFromBasketRes>> doOrderFromBasket(@RequestBody PostOrderFromBasketReq postOrderFromBasketReq) {
        try{
            int userIdx = jwtService.getUserIdx();
            List<PostOrderFromBasketRes> postOrderFromBasketRes = orderService.doOrderFromBasket(userIdx, postOrderFromBasketReq);
            return new BaseResponse<>(postOrderFromBasketRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostOrderRes> doOrder(@RequestBody PostOrderReq postOrderReq) {
        try{
            int userIdx = jwtService.getUserIdx();
            PostOrderRes postOrderRes = orderService.doOrder(userIdx, postOrderReq);
            return new BaseResponse<>(postOrderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetOrderRes>> getHistory(@RequestParam(value = "cursor") int cursor){
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetOrderRes> getOrderRes = orderProvider.getHistory(userIdx,cursor);
            return new BaseResponse<>(getOrderRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}

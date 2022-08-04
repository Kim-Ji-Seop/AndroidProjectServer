package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderFromBasketReq;
import com.example.demo.src.order.model.PostOrderFromBasketRes;
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
    @PostMapping("/{userIdx}")
    public BaseResponse<List<PostOrderFromBasketRes>> doOrderFromBasket(@PathVariable("userIdx") int userIdx,
                                                              @RequestParam(value = "basketId",required = false) List<Integer> basketId,
                                                              @RequestBody PostOrderFromBasketReq postOrderFromBasketReq) {
        try{
            List<PostOrderFromBasketRes> postOrderFromBasketRes = orderService.doOrder(userIdx,basketId, postOrderFromBasketReq);
            return new BaseResponse<>(postOrderFromBasketRes);
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

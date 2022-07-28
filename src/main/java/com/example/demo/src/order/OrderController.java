package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public BaseResponse<List<PostOrderRes>> doOrder(@PathVariable("userIdx") int userIdx, @RequestParam(value = "basketId") List<Integer> basketId,@RequestBody PostOrderReq postOrderReq) {
        System.out.println(userIdx);
        System.out.println(basketId);
        try{
            List<PostOrderRes> postOrderRes = orderService.doOrder(userIdx,basketId,postOrderReq);
            return new BaseResponse<>(postOrderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

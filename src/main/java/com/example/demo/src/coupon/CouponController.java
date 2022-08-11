package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.basket.model.PostBasketProductRes;
import com.example.demo.src.coupon.model.*;
import com.example.demo.src.order.OrderProvider;
import com.example.demo.src.order.OrderService;
import com.example.demo.src.order.model.PostOrderFromBasketRes;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/coupon")
public class CouponController {
    @Autowired
    private final CouponProvider couponProvider;
    @Autowired
    private final CouponService couponService;
    @Autowired
    private final JwtService jwtService;
    public CouponController(CouponProvider couponProvider,CouponService couponService,JwtService jwtService){
        this.couponProvider=couponProvider;
        this.couponService=couponService;
        this.jwtService=jwtService;
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostCouponRes> postCoupon(@RequestBody PostCouponReq postCouponReq){
        try{
            PostCouponRes postCouponRes = couponService.postCoupon(postCouponReq);
            return new BaseResponse<>(postCouponRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCouponRes>> getCoupon(){
        try{
            List<GetCouponRes> getCouponResList = couponProvider.getCoupon();
            return new BaseResponse<>(getCouponResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/individual")
    public BaseResponse<PostCouponIndividualRes> postCouponIndividual(@RequestBody PostCouponIndividualReq postCouponIndividualReq){
        try{
            int userIdx = jwtService.getUserIdx();
            PostCouponIndividualRes postCouponIndividualRes = couponService.postCouponIndividual(userIdx,postCouponIndividualReq);
            return new BaseResponse<>(postCouponIndividualRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @GetMapping("/individual")
    public BaseResponse<List<GetCouponIndividualRes>> getCouponIndividual(){
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetCouponIndividualRes> getCouponIndividualResList = couponProvider.getCouponIndividual(userIdx);
            return new BaseResponse<>(getCouponIndividualResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

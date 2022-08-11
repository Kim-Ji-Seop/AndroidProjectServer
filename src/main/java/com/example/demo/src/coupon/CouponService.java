package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.src.coupon.model.PostCouponIndividualReq;
import com.example.demo.src.coupon.model.PostCouponIndividualRes;
import com.example.demo.src.coupon.model.PostCouponReq;
import com.example.demo.src.coupon.model.PostCouponRes;
import com.example.demo.src.order.OrderDao;
import com.example.demo.src.order.OrderProvider;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CouponService {
    private final CouponDao couponDao;
    private final CouponProvider couponProvider;
    public CouponService(CouponDao couponDao,CouponProvider couponProvider){
        this.couponDao=couponDao;
        this.couponProvider=couponProvider;
    }

    public PostCouponRes postCoupon(PostCouponReq postCouponReq) throws BaseException {
        try {
            return couponDao.postCoupon(postCouponReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public PostCouponIndividualRes postCouponIndividual(int userIdx,PostCouponIndividualReq postCouponIndividualReq) throws BaseException {
        try {
            return couponDao.postCouponIndividual(userIdx,postCouponIndividualReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.src.coupon.model.GetCouponIndividualRes;
import com.example.demo.src.coupon.model.GetCouponRes;
import com.example.demo.src.order.OrderDao;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CouponProvider {
    private final CouponDao couponDao;

    public CouponProvider(CouponDao couponDao){
        this.couponDao=couponDao;
    }

    public List<GetCouponRes> getCoupon() throws BaseException{
        try {
            return couponDao.getCoupon();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCouponIndividualRes> getCouponIndividual(int userIdx) throws BaseException{
        try {
            return couponDao.getCouponIndividual(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

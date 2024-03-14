package com.atguigu.spzx.pay.mapper;

import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentInfoMapper {
    public void addPaymentInfo(PaymentInfo paymentInfo);

    PaymentInfo getByOrderNo(String orderNo);

    void afterPaySuccess(@Param("tradeNo") String trade_no, @Param("orderNo") String orderNo,@Param("content") String content);
}

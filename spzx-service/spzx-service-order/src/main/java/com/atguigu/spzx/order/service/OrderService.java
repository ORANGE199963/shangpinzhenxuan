package com.atguigu.spzx.order.service;

import com.atguigu.spzx.model.dto.h5.OrderInfoDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.github.pagehelper.PageInfo;

public interface OrderService {
    TradeVo trade();

    Long submitOrder(OrderInfoDto orderInfoDto);

    OrderInfo getByOrderId(Long orderId);

    PageInfo findPage(Integer pageNum, Integer pageSize, Integer orderStatus);

    TradeVo buy(Long skuId);

    void afterPaySuccess(String orderNo, Integer payType);

    OrderInfo getByOrderNo(String orderNo);

    void cancelOrder(String orderNo, String reason);
}

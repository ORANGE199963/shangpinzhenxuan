package com.atguigu.spzx.order.mapper;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.entity.order.OrderLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    //添加订单
    void addOrder(OrderInfo orderInfo);

    //添加订单明细
    void addOrderItem(OrderItem orderItem);

    void addOrderLog(OrderLog orderLog);

    OrderInfo getOrderInfoById(Long orderId);

    List<OrderItem> getOrderItemListByOrderId(Long orderId);

    List<OrderInfo> findList(@Param("orderStatus") Integer orderStatus,@Param("userId") Long userId);

    OrderInfo getByOrderNo(String orderNo);

    void afterPaySuccess(@Param("orderNo") String orderNo,@Param("payType") Integer payType);

    void cancelOrder(@Param("orderNo") String orderNo,@Param("reason") String reason);
}

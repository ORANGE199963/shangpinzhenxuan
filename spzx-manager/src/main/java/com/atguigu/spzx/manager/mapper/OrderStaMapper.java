package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderStaMapper {
    List<OrderStatistics> selectOrderStaList(OrderStatisticsDto orderStatisticsDto);

    void addOrderSta(OrderStatistics orderStatistics);

    Integer getByPreDate();
}

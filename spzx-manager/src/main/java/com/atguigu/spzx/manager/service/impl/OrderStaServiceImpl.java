package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.OrderStaMapper;
import com.atguigu.spzx.manager.service.OrderStaService;
import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderStaServiceImpl implements OrderStaService {

    @Autowired
    OrderStaMapper orderStaMapper;


    @Override
    public Map getOrderSta(OrderStatisticsDto orderStatisticsDto) {
        List<OrderStatistics> list = orderStaMapper.selectOrderStaList(orderStatisticsDto);

        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<BigDecimal> amountList = new ArrayList<>();

        for (OrderStatistics orderStatistics : list) {
            Date orderDate = orderStatistics.getOrderDate();
            dateList.add(new DateTime(orderDate).toString("yyyy-MM-dd"));
            amountList.add(orderStatistics.getTotalAmount());
        }
        Map map = new HashMap();
        map.put("dateList",dateList);
        map.put("amountList",amountList);
        return map;
    }

}

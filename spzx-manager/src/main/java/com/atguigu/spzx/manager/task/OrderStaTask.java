package com.atguigu.spzx.manager.task;

import com.atguigu.spzx.manager.mapper.OrderInfoMapper;
import com.atguigu.spzx.manager.mapper.OrderStaMapper;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderStaTask {


    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    OrderStaMapper orderStaMapper;

    @Scheduled(cron = "2 * * * * ?")
    public void orderSta() {
        String preDate = new DateTime().plusDays(-1).toString("yyyy-MM-dd");
        OrderStatistics orderStatistics = orderInfoMapper.getOrderSta(preDate);

        if (orderStatistics != null) {
            Integer count = orderStaMapper.getByPreDate();
            if (count == 0) {
                orderStaMapper.addOrderSta(orderStatistics);
            }
        }
        System.out.println(new DateTime());
    }
}

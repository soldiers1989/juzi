package com.jzfq.retail.core.task.timer;

import com.jzfq.retail.common.enmu.OrderStatus;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @Author MaoLixia
 * @Date 2018/8/15 15:47
 */
@Slf4j
public class OrderAwaitPayToCancleTimer extends QuartzJobBean {
    @Autowired
    OrdersBaseService ordersBaseService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("定时任务，将待支付订单状态更新为已取消 110 -》 200  每一小时执行一次，开始时间：{}", new Date());
        ordersBaseService.changeOrderStatus(OrderStatus.ORDER_STATE_110.getCode(), OrderStatus.ORDER_STATE_200.getCode(), 48*60);
        log.info("定时任务，将待支付订单状态更新为已取消 110 -》 200  每一小时执行一次，结束时间：{}", new Date());
    }
}

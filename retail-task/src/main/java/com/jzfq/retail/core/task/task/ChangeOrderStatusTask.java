package com.jzfq.retail.core.task.task;

import com.jzfq.retail.core.api.service.ErrorRetryTaskService;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @title: ErrorRetryTask
 * @description:
 * @company: 北京桔子分期电子商务有限公司
 * @author: Liu Wei
 * @date: 2018/7/3 18:11
 */
@Component
public class ChangeOrderStatusTask {

//    @Autowired
//    OrdersBaseService ordersBaseService;

//    {秒} {分} {时} {日} {月} {周} {年(可选)}
//    每隔5秒执行一次：*/5 * * * * ?
//    在26分、29分、33分执行一次：0 26,29,33 * * * ?
//    每5分钟执行一次  (cron = "0 0/5 * * * ?")

    /**
     * 每天凌晨执行一次 变更订单状态 定时任务，将待收货状态自动变成已收货 155 -》 160
     */
//    @Scheduled(cron= "0 0 1 * * ?")
    private void process(){
//        ordersBaseService.ChangeOrderStatus();
    }
}

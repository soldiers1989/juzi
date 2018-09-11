package com.jzfq.retail.core.task.timer;

import com.jzfq.retail.core.api.service.OrdersBaseService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 定时任务，将待收货状态自动变成已收货 155 -》 160  每天凌晨执行一次
 * Date：2018/8/8
 * Time：17:47
 * @author LiuWei
 */
@Slf4j
public class ChangeStatusTimer extends QuartzJobBean {

    @Autowired
    OrdersBaseService ordersBaseService;

    /**
     * 定时任务逻辑实现方法
     * 每当触发器触发时会执行该方法逻辑
     *
     * @param jobExecutionContext 任务执行上下文
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("定时任务，将待收货状态自动变成已收货 155 -》 160  每天凌晨执行一次，任务时间：{}", new Date());
        ordersBaseService.ChangeOrderStatus();
    }
}
